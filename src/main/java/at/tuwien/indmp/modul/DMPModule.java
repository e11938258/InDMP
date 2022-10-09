package at.tuwien.indmp.modul;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import at.tuwien.indmp.exception.BadRequestException;
import at.tuwien.indmp.exception.ConflictException;
import at.tuwien.indmp.exception.ForbiddenException;
import at.tuwien.indmp.exception.NotFoundException;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.model.dmp.DMP;
import at.tuwien.indmp.model.dmp.DMPScheme;
import at.tuwien.indmp.util.Functions;
import at.tuwien.indmp.util.ModelConstants;

@Service
public class DMPModule {

    @Value("${identification.by-creation-only}")
    private boolean byCreationOnly;

    @Value("${identification.by-identifier-only}")
    private boolean byIdentifierOnly;

    @Autowired
    PropertyModule propertyModule;

    private final Logger log = LoggerFactory.getLogger(DMPModule.class);

    /**
     * 
     * Create a new DMP with all properties
     * 
     * @param dmp
     * @param dataService
     */
    public void create(DMP dmp, RDMService dataService) {
        if (findMaDMPIdentifier(dmp.getObjectIdentifier()) == null) {
            // Get all properties from the new DMP
            final List<Property> properties = dmp.getProperties(dmp, "", dataService);
            properties.addAll(dmp.getPropertiesFromNestedObjects(dmp, "", dataService));

            // Persist all properties
            propertyModule.persistList(properties, dataService);
        } else {
            throw new ConflictException("DMP is already created.");
        }
    }

    /**
     * 
     * UC7: Validation and identification of the received maDMP.
     * 
     * @param dmp
     * @param rdmService
     * @return minimum of DMP
     */
    public void validateAndIdentifyMaDMP(@Valid DMP dmp, RDMService rdmService) {
        // 1. The integration service checks whether the maDMP contains the mandatory
        // properties.
        checkMinimalDMP(dmp);

        // 2. If the modified property is future
        if (dmp.getModified().isAfter(LocalDateTime.now())) {
            // 2.1 The integration service returns an error message to the RDM service and
            // terminates the process
            log.error("The wrong modified property.");
            throw new BadRequestException("The wrong modified property.");
        }

        // 3. If the property created is not the same as modified
        if (!dmp.isNew()) {
            // 3.1 The integration service tries to identify the maDMP using the property
            // created.
            List<Property> creationProperties = findCreationProperties(dmp.getCreatedInString());

            // 3.2 If maDMP was identified
            if (creationProperties.size() > 0) {

                // 3.2.1 The integration service tries to identify the maDMP using the
                // identifier
                final Property madmpIdentifier = findMaDMPIdentifier(dmp.getDmp_id().getIdentifier());

                // 3.2.2 If maDMP was not identified by the identifier
                if (madmpIdentifier == null) {

                    // 3.2.2.1 If identification only by the property created is not allowed
                    if (!byCreationOnly) {
                        // 3.2.2.1.1 The integration service returns an error message to the RDM service
                        // and terminates the process.
                        log.error("DMP not found by identifier.");
                        throw new NotFoundException("DMP not found by identifier.");
                    } else {
                        // 3.2.2.2 Else

                        // 3.2.2.2.1 If multiple values of the created property were found
                        if (creationProperties.size() > 1) {
                            // 3.2.2.2.1.1 The integration service returns an error message to the RDM
                            // service and terminates the process.
                            log.error("Multiple creation dates");
                            throw new NotFoundException("Multiple creation dates");
                        } else {
                            // 3.2.2.2.2 Else

                            // 3.2.2.2.2.1 The integration service corrects the maDMP identifier in storage.
                            final Property newIdentifier = Functions.propertyMaker(dmp,
                                    creationProperties.get(0).getAtLocation(), "dmp:identifier",
                                    dmp.getObjectIdentifier());
                            changeObjectIdentifier(dmp, newIdentifier, rdmService);

                        }

                    }
                }
            } else {
                // 3.3 Else

                // 3.3.1 If identification only by identifier is not allowed.
                if (!byIdentifierOnly) {
                    // 3.3.1.1 The integration service returns an error message to the RDM service
                    // and terminates the process
                    log.error("DMP not found by creation date.");
                    throw new NotFoundException("DMP not found by creation date.");
                } else {
                    // 3.3.2 Else

                    // 3.3.2.1 The integration service tries to identify the maDMP using an
                    // identifier.
                    final Property madmpIdentifier = findMaDMPIdentifier(dmp.getDmp_id().getIdentifier());

                    // 3.3.2.2 If maDMP was not identified
                    if (madmpIdentifier == null) {
                        // 3.3.2.2.1 The integration service returns an error message to the RDM service
                        // and terminates the process.
                        log.error("DMP not found by either identifier or creation date.");
                        throw new NotFoundException("DMP not found by either identifier or creation date.");
                    }

                }
            }
        }
    }

    /**
     * 
     * Check if modified property is newer than the stored one
     * 
     * @param dmp
     * 
     */
    public void checkModifiedProperty(DMP dmp) {
        // Load the original modified property
        final Property originalModifiedProperty = propertyModule.findProperty(dmp.getAtLocation(""), "dmp:modified",
                null, true);

        LocalDateTime originalModified = null;
        if (originalModifiedProperty == null) {
            throw new BadRequestException("Cannot find the stored modified property.");
        } else {
            originalModified = LocalDateTime.parse(originalModifiedProperty.getValue());
        }

        // Is the old version of the DMP?
        if (dmp.getModified() == null || originalModified.isAfter(dmp.getModified())) {
            throw new ConflictException("There is a newer version of maDMP.");
        }
    }

    /**
     * 
     * Build the dmp
     * 
     * @param dmp
     * @return
     */
    public DMPScheme loadWholeDMP(DMP dmp) {
        // Build DMP
        final DMP fullDMP = new DMP();
        fullDMP.build(propertyModule, dmp.getAtLocation(""));

        // Return in the DMP scheme
        final DMPScheme dmpScheme = new DMPScheme();
        dmpScheme.setDmp(fullDMP);
        return dmpScheme;
    }

    /**
     * 
     * Update the dmp
     * 
     * @param dmp
     * @param rdmService
     */
    public void update(DMP dmp, RDMService rdmService) {
        // Get properties from the DMP
        final List<Property> properties = dmp.getProperties(dmp, "", rdmService);
        properties.addAll(dmp.getPropertiesFromNestedObjects(dmp, "", rdmService));

        // Set new properties
        propertyModule.terminateAndCreateProperties(properties, rdmService);
    }

    /**
     *
     * Change the object identifier
     *
     * @param dmp
     * @param property
     * @param rdmService
     */
    public void changeObjectIdentifier(DMP dmp, Property property, RDMService rdmService) {
        Objects.requireNonNull(dmp, "DMP is null.");
        Objects.requireNonNull(property, "Identifier is null.");
        Objects.requireNonNull(rdmService, "Service is null.");

        // 8.3 If (the message contains the correct information and) the identifier is
        // changeable
        if (ModelConstants.IDENTIFIER_CHANGEABLE_CLASSES.contains(property.getSpecializationOf())) {

            // 8.3.1 The integration service checks whether the RDM service has the right to
            // change this property (identifier).
            // 8.3.2 If the RDM service has the right to change the property
            if (rdmService.getPropertyRights().contains(property.getSpecializationOf())) {

                // 8.3.2.1 The integration service finds the stored identifier.
                final Property currentIdentifier = propertyModule.findProperty(property.getAtLocation(),
                        property.getSpecializationOf(), null, true);

                // 8.3.2.2 If the identifier was found and the values are different
                if (currentIdentifier != null && !currentIdentifier.getValue().equals(property.getValue())) {

                    // 8.3.2.2.1 The integration service terminates the previous value of the
                    // property modified and creates a new record with the received value
                    final List<Property> properties = dmp.getProperties(dmp, "", rdmService);
                    final Property modifiedProperty = Functions.findPropertyInList("dmp:modified", properties);
                    propertyModule.terminateAndCreateProperty(modifiedProperty, rdmService);

                    // 8.3.2.2.2 The integration service terminates the previous value of the
                    // identifier and creates a new record with the received value
                    final String oldLocation = currentIdentifier.getAtLocation();
                    final String location = oldLocation.replace(currentIdentifier.getValue(), property.getValue());
                    final Property newIdentifier = Functions.propertyMaker(dmp, oldLocation,
                            currentIdentifier.getSpecializationOf(), property.getValue());
                    propertyModule.terminateAndCreateProperty(newIdentifier, rdmService);

                    // 8.3.2.2.3 The integration service changes all references to this identifier
                    propertyModule.changeNestedProperties(oldLocation, location);
                } else {
                    log.error("Cannot find identifier at location " + property.getAtLocation());
                    throw new NotFoundException("Cannot find identifier at location " + property.getAtLocation());
                }
            } else {
                log.error("Service does not have rights to update the identifier in the object.");
                throw new BadRequestException("Service does not have rights to update the identifier in the object.");
            }
        } else {
            log.error("Cannot change " + property.getSpecializationOf());
            throw new ForbiddenException("Cannot change " + property.getSpecializationOf());

        }
    }

    /**
     *
     * Delete the maDMP object by identifier property
     *
     * @param dmp
     * @param property
     * @param rdmService
     */
    public void deleteInstance(DMP dmp, Property property, RDMService rdmService) {
        Objects.requireNonNull(dmp, "DMP is null.");
        Objects.requireNonNull(property, "Identifier is null.");
        Objects.requireNonNull(rdmService, "Service is null.");

        // 8.3 If (the message contains the correct information and) the object is
        // removable
        if (ModelConstants.REMOVABLE_CLASSES.containsKey(property.getSpecializationOf())) {

            // 8.3.1 The integration service finds all active properties belonging to the
            // object.
            final List<Property> objectProperties = propertyModule.findProperties(property.getAtLocation(), null, null,
                    true);
            final Property objectIdentifier = Functions.findPropertyInList(
                    ModelConstants.REMOVABLE_CLASSES.get(property.getSpecializationOf()),
                    objectProperties);

            // 8.3.2 If the active identifier was found
            if (objectIdentifier != null) {

                // 8.3.2.1 The integration service finds all active properties depending on the
                // object.
                final List<Property> properties = propertyModule.findAllProperties(property.getAtLocation(), null,
                        true);

                // 8.3.2.2 The integration service checks whether the RDM service has the right
                // to modify all properties of the object as well as of objects that are
                // dependent to it
                for (final Property p : properties) {
                    if (!rdmService.getPropertyRights().contains(p.getSpecializationOf())) {
                        log.error("The RDM service does not have the right to delete " + p.getSpecializationOf());
                        throw new BadRequestException(
                                "The RDM service does not have the right to delete " + p.getSpecializationOf());
                    }
                }

                // 8.3.2.3 If the RDM service has the right to remove all properties

                // 8.3.2.3.1 The integration service terminates the previous value of the
                // property modified and creates a new record with the received value.
                final List<Property> dmpProperties = dmp.getProperties(dmp, "", rdmService);
                final Property modifiedProperty = Functions.findPropertyInList("dmp:modified", dmpProperties);
                propertyModule.terminateAndCreateProperty(modifiedProperty, rdmService);

                // 8.3.2.3.2 The integration service terminates all properties of dependent
                // objects
                // 8.3.2.3.3 The integration service terminates all properties of the object
                propertyModule.terminateAllProperties(property.getAtLocation(), dmp.getModified(), rdmService);

            } else {
                log.error("Object not found.");
                throw new BadRequestException("Object not found.");
            }
        }
    }

    /**
     * 
     * Get all maDMP identifiers
     * 
     * @return
     */
    public List<Property> getAllMaDMPs() {
        return propertyModule.findProperties(null, "dmp:identifier", null, true);
    }

    /**
     *
     * Get the provenance information
     *
     * @param dmp
     * @param property
     * @return
     */
    public List<Property> getProvenanceInformation(DMP dmp, Property property) {
        Objects.requireNonNull(dmp);
        Objects.requireNonNull(property);
        return propertyModule.findAllProperties(dmp.getAtLocation(""), property.getSpecializationOf(), false);
    }

    /* Private */

    private void checkMinimalDMP(DMP dmp) {
        if (dmp == null || dmp.getCreated() == null || dmp.getDmp_id() == null
                || dmp.getModified() == null
                || dmp.getDmp_id().getObjectIdentifier() == null
                || dmp.getDmp_id().getObjectIdentifier().length() == 0) {
            // 2. If the maDMP does not contain a mandatory properties
            // 2.1 The integration service returns an error message to the RDM service and
            // terminates the process.
            log.error("Missing the mandatory maDMP properties.");
            throw new BadRequestException("Missing the mandatory maDMP properties.");
        }
    }

    private List<Property> findCreationProperties(String creationDate) {
        return propertyModule.findProperties(null, "dmp:created", creationDate, true);
    }

    private Property findMaDMPIdentifier(String identifier) {
        return propertyModule.findProperty(null, "dmp:identifier", identifier, true);
    }

}
