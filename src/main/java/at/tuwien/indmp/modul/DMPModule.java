package at.tuwien.indmp.modul;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import at.tuwien.indmp.exception.BadRequestException;
import at.tuwien.indmp.exception.ConflictException;
import at.tuwien.indmp.exception.ForbiddenException;
import at.tuwien.indmp.exception.NotFoundException;
import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.dmp.DMP;
import at.tuwien.indmp.model.dmp.DMPScheme;
import at.tuwien.indmp.model.dmp.DMP_id;
import at.tuwien.indmp.util.ModelConstants;
import at.tuwien.indmp.util.Functions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DMPModule {

    @Value("${identification.by-creation-only}")
    private boolean byCreationOnly;

    @Value("${identification.auto-correction}")
    private boolean autoCorrection;

    @Value("${identification.by-identifier-only}")
    private boolean byIdentifierOnly;

    @Autowired
    PropertyModule propertyModule;

    private final Logger log = LoggerFactory.getLogger(DMPModule.class);

    /**
     * 
     * Create a new DMP
     * 
     * @param dmp
     * @param dataService
     */
    public void create(DMP dmp, RDMService dataService) {
        if (findByIdentifier(dmp.getObjectIdentifier()) == null) {
            // Get properties from new DMP
            final List<Property> properties = dmp.getProperties(dmp, "", dataService);
            properties.addAll(dmp.getPropertiesFromNestedObjects(dmp, "", dataService));

            // Persist the properties
            propertyModule.persist(properties, dataService);
        } else {
            throw new ConflictException("DMP is already created.");
        }
    }

    /**
     * 
     * Identify DMP by creation date and identifier
     * 
     * @param dmp
     * @param dataService
     * @return minimum of DMP
     */
    public DMP identifyDMP(@Valid DMP dmp, RDMService dataService) {
        // Check minimal DMP
        checkMinimalDMP(dmp);
        // Is creation date same as modification date?
        DMP currentDMP = null;
        if (dmp.isNew()) {
            return null; // New DMP
        } else {
            // Identify by creation date
            List<Property> currentCreationDates = findByCreationDate(dmp.getCreatedInString());
            // Identified by creation date?
            if (currentCreationDates.size() > 0) {
                // Find DMP Identifier
                currentDMP = findByIdentifier(dmp.getDmp_id().getIdentifier());
                // Identifier found?
                if (currentDMP != null) {
                    return currentDMP;
                } else if (currentCreationDates.size() == 1) { // Just one creation date?
                    // Load details by creation date
                    currentDMP = loadMinimalDMP(currentCreationDates.get(0).getAtLocation());
                    // Auto correction enabled?
                    if (autoCorrection && dataService != null) {
                        changeIdentifiers(dmp, Functions.createProperty(currentDMP, currentDMP.getAtLocation(""),
                                "dmp:identifier", dmp.getObjectIdentifier()), dataService);
                    }

                    // Identification by creation date only?
                    if (byCreationOnly) {
                        return currentDMP;
                    } else {
                        log.error("DMP not found by identifier " + dmp.getObjectIdentifier() + ", creation date: "
                                + currentDMP.getCreated().toString());
                        throw new NotFoundException("DMP not found by identifier.");
                    }
                } else {
                    log.error("Multiple creation dates");
                    throw new NotFoundException("Multiple creation dates");
                }
            } else {
                // Identify by DMP Identifier
                currentDMP = findByIdentifier(dmp.getDmp_id().getIdentifier());
                // Identified by DMP identifier?
                if (currentDMP != null) {
                    // Identification by identifier only?
                    if (byIdentifierOnly) {
                        return currentDMP;
                    } else {
                        log.error("DMP not found by creation date, identifier: " + currentDMP.getObjectIdentifier());
                        throw new NotFoundException("DMP not found by creation date.");
                    }
                } else {
                    log.error("DMP not found by either identifier or creation date.");
                    throw new NotFoundException("DMP not found by either identifier or creation date.");
                }
            }
        }
    }

    private void checkMinimalDMP(DMP dmp) {
        if (dmp == null || dmp.getCreated() == null || dmp.getModified() == null || dmp.getDmp_id() == null
                || dmp.getDmp_id().getObjectIdentifier() == null
                || dmp.getDmp_id().getObjectIdentifier().length() == 0) {
            log.error("Missing minimum maDMP.");
            throw new BadRequestException("Missing minimum maDMP.");
        }
    }

    private List<Property> findByCreationDate(String creationDate) {
        return propertyModule.findEntities(null, "dmp:created", creationDate, true);
    }

    private DMP findByIdentifier(String identifier) {
        final Property entity = propertyModule.findEntity(null, "dmp:identifier", identifier);
        if (entity != null) {
            return loadMinimalDMP(entity.getAtLocation());
        } else {
            return null;
        }
    }

    private DMP loadMinimalDMP(String atLocation) {
        // Find mandatory properties
        final List<Property> properties = propertyModule.findEntities(atLocation, null, null, true);
        final String created = Functions.findPropertyInList("dmp", "created", properties).getValue();
        final String modified = Functions.findPropertyInList("dmp", "modified", properties).getValue();
        final String identifier = Functions.findPropertyInList("dmp", "identifier", properties).getValue();

        // Create a new minimal DMP
        final DMP_id dmp_id = new DMP_id(identifier);
        return new DMP(LocalDateTime.parse(created), LocalDateTime.parse(modified), dmp_id);
    }

    /**
     * 
     * Check if modified property is newer than the stored one, but not future
     * 
     * @param originModified
     * @param newModified
     * 
     */
    public void checkModifiedProperty(LocalDateTime originModified, LocalDateTime newModified) {
        if (originModified.equals(newModified) || originModified.isAfter(newModified)) {
            throw new ConflictException("There is a newer version of maDMP.");
        } else if (LocalDateTime.now().isBefore(newModified)) {
            throw new ForbiddenException("Cannot use future time.");
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
        final DMP wholeDMP = new DMP();
        wholeDMP.build(propertyModule, dmp.getAtLocation(""));

        // Return in the DMP scheme
        final DMPScheme dmpScheme = new DMPScheme();
        dmpScheme.setDmp(wholeDMP);
        return dmpScheme;
    }

    /**
     * 
     * Update dmp
     * 
     * @param currentDMP
     * @param dmp
     * @param dataService
     */
    public void update(DMP dmp, RDMService dataService) {
        // Get properties from new DMP
        final List<Property> properties = dmp.getProperties(dmp, "", dataService);
        properties.addAll(dmp.getPropertiesFromNestedObjects(dmp, "", dataService));

        // Set new properties
        propertyModule.deactivateAndCreateEntities(properties, dataService);
    }

    /**
     *
     * Change identifier
     *
     * @param dmp         is the new DMP
     * @param entity
     * @param dataService
     */
    public void changeIdentifiers(DMP dmp, Property entity, RDMService dataService) {
        Objects.requireNonNull(dmp, "DMP is null.");
        Objects.requireNonNull(entity, "Identifier is null.");
        Objects.requireNonNull(dataService, "Service is null.");

        // Is the identifier changeable for the this class?
        if (ModelConstants.IDENTIFIER_CHANGEABLE_CLASSES.contains(entity.getSpecializationOf())) {

            // Has service rights to update?
            if (hasRights(entity, dataService)) {

                // Find identifier
                final Property currentIdentifier = propertyModule.findEntity(entity.getAtLocation(),
                        entity.getSpecializationOf(), null);

                // Found?
                if (currentIdentifier != null) {
                    // Change the modified property
                    updateModified(dmp, dataService);

                    // Create a new location
                    final String oldLocation = currentIdentifier.getAtLocation();
                    final String location = oldLocation.replace(currentIdentifier.getValue(), entity.getValue());

                    // Update entity
                    propertyModule.deactivateAndCreateEntity(
                            Functions.createProperty(dmp, oldLocation, currentIdentifier.getSpecializationOf(),
                                    entity.getValue()),
                            dataService);

                    // Change nested locations
                    propertyModule.changeNestedEntities(oldLocation, location);
                } else {
                    log.error("Cannot find identifier at location " + entity.getAtLocation());
                    throw new NotFoundException("Cannot find identifier at location " + entity.getAtLocation());
                }
            } else {
                log.error("Service does not have rights to update the identifier in this class.");
                throw new ForbiddenException("Service does not have rights to update the identifier in this class.");
            }
        } else {
            log.error("Cannot change " + entity.getSpecializationOf());
            throw new BadRequestException("Cannot change " + entity.getSpecializationOf());
        }
    }

    /**
     *
     * Delete instance
     *
     * @param entidmpty
     * @param entity
     * @param dataService
     */
    public void deleteInstance(DMP dmp, Property entity, RDMService dataService) {
        Objects.requireNonNull(entity, "Entity is null.");

        // Removable class?
        if (ModelConstants.REMOVABLE_CLASSES.contains(entity.getSpecializationOf())) {

            // Has service rights to delete?
            if (hasRights(entity, dataService)) {

                // Find location
                final List<Property> entities = propertyModule.findEntities(entity.getAtLocation(), null, null, true);

                if (entities.size() > 0) {
                    // Change the modified property
                    updateModified(dmp, dataService);
                    // Remove entities
                    propertyModule.removeAllNestedEntities(entity.getAtLocation(), dmp.getModified());
                } else {
                    log.error("Cannot find location " + entity.getAtLocation());
                    throw new NotFoundException("Cannot find location " + entity.getAtLocation());
                }
            } else {
                log.error("Service does not have rights to delete the class.");
                throw new ForbiddenException("Service does not have rights to delete the class.");
            }
        } else {
            log.error("Cannot delete " + entity.getSpecializationOf());
            throw new BadRequestException("Cannot delete " + entity.getSpecializationOf());
        }
    }

    private boolean hasRights(Property entity, RDMService dataService) {
        // for (String right : dataService.getRights()) {
        //     if (entity.getSpecializationOf().contains(right)) {
        //         return true;
        //     }
        // }
        return false;
    }

    private void updateModified(DMP dmp, RDMService dataService) {
        propertyModule.deactivateAndCreateEntity(Functions.findPropertyInList("dmp", "modified",
                dmp.getProperties(dmp, "", dataService)), dataService);
    }

    /**
     *
     * Load DMP identifiers with history
     *
     * @param dmp
     * @return
     */
    public List<Property> loadIdentifierHistory(DMP dmp) {
        Objects.requireNonNull(dmp);

        final List<Property> entities = new ArrayList<>();

        // For each changeable class identifier
        for (String specializationOf : ModelConstants.IDENTIFIER_CHANGEABLE_CLASSES) {
            entities.addAll(propertyModule.findAllEntities(dmp.getAtLocation(""), specializationOf, false));
        }

        return entities;
    }
}
