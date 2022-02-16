package at.tuwien.indmp.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import at.tuwien.indmp.exception.BadRequestException;
import at.tuwien.indmp.exception.NotFoundException;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.model.dmp.DMP;
import at.tuwien.indmp.model.dmp.DMPScheme;
import at.tuwien.indmp.model.dmp.DMP_id;
import at.tuwien.indmp.model.idmp.Identifier;
import at.tuwien.indmp.model.idmp.IdentifierUnit;
import at.tuwien.indmp.util.DMPConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DMPService {

    @Value("${identification.by-creation-only}")
    private boolean byCreationOnly;

    @Value("${identification.auto-correction}")
    private boolean autoCorrection;

    @Value("${identification.by-identifier-only}")
    private boolean byIdentifierOnly;

    @Autowired
    PropertyService propertyService;

    @Autowired
    RDMServiceLayer rdmServiceLayer;

    private final Logger log = LoggerFactory.getLogger(DMPService.class);

    /**
     * 
     * Create a new DMP
     * 
     * @param dmp
     * @param rdmService
     */
    public void create(DMP dmp, RDMService rdmService) {
        // Get properties from new DMP
        final List<Property> properties = dmp.getProperties(dmp, null, rdmService);
        properties.addAll(dmp.getPropertiesFromNestedClasses(dmp, rdmService));

        // Persist the properties
        propertyService.persist(properties, rdmService);
    }

    /**
     * 
     * Identify DMP by creation date and identifier
     * 
     * @param dmp
     * @param system
     * @return minimum of DMP
     */
    public DMP identifyDMP(DMP dmp, RDMService system) {
        // Check minimal DMP
        checkMinimalDMP(dmp);
        // Is creation date same as modification date?
        DMP currentDMP = null;
        if (dmp.getCreated().equals(dmp.getModified())) {
            return null; // New DMP
        } else {
            // Identify by creation date
            currentDMP = findByCreationDate(dmp.getCreatedInString());
            // Identified by creation date?
            if (currentDMP != null) {
                // Are the DMP identifiers same?
                if (dmp.areSame(currentDMP.getClassIdentifier())) {
                    return currentDMP;
                } else {
                    // Auto correction enabled?
                    if (autoCorrection && system != null) {
                        List<IdentifierUnit> identifier = new ArrayList<>();
                        identifier.add(new IdentifierUnit("dmp",
                                new Identifier(currentDMP.getDmp_id().getIdentifier(),
                                        currentDMP.getDmp_id().getType().toString()),
                                new Identifier(dmp.getDmp_id().getIdentifier(),
                                        dmp.getDmp_id().getType().toString())));
                        changeIdentifiers(currentDMP, identifier, dmp.getModified(), system);
                    }

                    // Identification by creation only?
                    if (byCreationOnly) {
                        return currentDMP;
                    } else {
                        log.error("DMP not found by identifier, creation date: " + currentDMP.getCreated().toString());
                        throw new NotFoundException("DMP not found by identifier.");
                    }
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
                        log.error("DMP not found by creation date, identifier: " + currentDMP.getClassIdentifier());
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
        if (dmp.getCreated() == null || dmp.getModified() == null || dmp.getDmp_id() == null
                || dmp.getDmp_id().getClassIdentifier() == null) {
            log.error("Missing minimum maDMP.");
            throw new BadRequestException("Missing minimum maDMP.");
        }
    }

    private DMP findByCreationDate(String creationDate) {
        Property property = propertyService.find(null, "dmp", null, "created", creationDate, null);
        if (property != null) {
            return loadMinimalDMP(property.getDmpIdentifier());
        } else {
            return null;
        }
    }

    private DMP findByIdentifier(String identifier) {
        Property property = propertyService.find(null, "dmp_id", null, "identifier", identifier, null);
        if (property != null) {
            return loadMinimalDMP(identifier);
        } else {
            return null;
        }
    }

    private DMP loadMinimalDMP(String dmpIdentifier) {
        final String created = propertyService.find(dmpIdentifier, "dmp", null, "created", null, null)
                .getValue();
        final String modified = propertyService.find(dmpIdentifier, "dmp", null, "modified", null, null)
                .getValue();
        final String identifier = propertyService.find(dmpIdentifier, "dmp_id", null, "identifier", null, null)
                .getValue();
        final String type = propertyService.find(dmpIdentifier, "dmp_id", null, "type", null, null).getValue();
        final DMP_id dmp_id = new DMP_id(identifier, type);
        try {
            return new DMP(DMPConstants.DATE_TIME_FORMATTER_ISO_8601.parse(created),
                    DMPConstants.DATE_TIME_FORMATTER_ISO_8601.parse(modified), dmp_id);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Update dmp
     * 
     * @param currentDMP
     * @param dmp
     */
    public void update(DMP currentDMP, DMP dmp, RDMService rdmService) {
        // Get properties from new DMP
        final List<Property> properties = dmp.getProperties(dmp, null, rdmService);
        properties.addAll(dmp.getPropertiesFromNestedClasses(dmp, rdmService));

        // Set new properties
        propertyService.updateOrCreateProperties(properties, rdmService);
    }

    /**
     * 
     * Update the property modified
     * 
     * @param currentDMP
     * @param modified
     * @param system
     */
    public void updateModified(DMP currentDMP, Date modified, RDMService system) {
        // Update the property
        propertyService.loadAndUpdate(currentDMP.getClassIdentifier(), "dmp", null, "modified",
                DMPConstants.DATE_TIME_FORMATTER_ISO_8601.format(modified), null, system);
    }

    /**
     * 
     * Change identifiers
     * 
     * @param currentDMP
     * @param identifiers
     * @param modified
     * @param rdmService
     */
    public void changeIdentifiers(DMP currentDMP, List<IdentifierUnit> identifiers, Date modified,
            RDMService rdmService) {
        Objects.requireNonNull(currentDMP, "DMP is null.");
        Objects.requireNonNull(identifiers, "Identifier array is null.");
        Objects.requireNonNull(rdmService, "RDM service is null.");

        // For each identifier
        for (IdentifierUnit identifier : identifiers) {

            // Is the identifier changeable for the this class?
            if (DMPConstants.identifierChangeableClasses.containsKey(identifier.getType())) {

                // Get data about identifier
                final String[] identifierData = DMPConstants.identifierChangeableClasses.get(identifier.getType());

                // Find property
                final Property currentIdentifier = propertyService.find(currentDMP.getClassIdentifier(),
                        identifierData[0], identifier.getId().getIdentifier(), identifierData[1],
                        identifier.getId().getIdentifier(), null);
                Property currentType = null;

                // Has identifier type?
                if (identifierData.length > 2) {
                    currentType = propertyService.find(currentDMP.getClassIdentifier(),
                            identifierData[0], identifier.getId().getIdentifier(), identifierData[2],
                            identifier.getId().getType(), null);
                }

                // Found?
                if (currentIdentifier != null && (identifierData.length == 2 || currentType != null)) {
                    // Set new properties
                    currentIdentifier.setValue(identifier.getNew_id().getIdentifier());
                    propertyService.update(currentIdentifier, rdmService);

                    if (currentType != null) {
                        currentType.setValue(identifier.getNew_id().getType());
                        propertyService.update(currentType, rdmService);
                    }

                    // Change class identifiers and references
                    propertyService.changeNestedIdentifiers(currentDMP.getClassIdentifier(),
                            identifier.getId().getIdentifier(),
                            identifier.getNew_id().getIdentifier());
                } else {
                    log.error("Cannot find identifier " + identifier.getId().getIdentifier() + " or its type.");
                }
            } else {
                log.warn("Cannot change identifier for type " + identifier.getType());
            }
        }
    }

    /**
     * 
     * Delete instances
     * 
     * @param currentDMP
     * @param identifiers
     * @param system
     */
    public void deleteInstances(DMP currentDMP, List<IdentifierUnit> identifiers) {
        Objects.requireNonNull(currentDMP, "DMP is null.");
        Objects.requireNonNull(identifiers, "Identifier array is null.");

        // For each identifier find instance and end properties
        for (IdentifierUnit identifier : identifiers) {
            propertyService.removeAllNestedProperties(currentDMP.getClassIdentifier(),
                    identifier.getId().getIdentifier());
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
        wholeDMP.build(propertyService, dmp.getClassIdentifier(), null);

        // Return in the right format
        final DMPScheme dmpScheme = new DMPScheme();
        dmpScheme.setDmp(wholeDMP);
        return dmpScheme;
    }

    /**
     * 
     * Load all DMP identifiers with history
     * 
     * @param dmp
     * @return
     */
    public List<Property> loadDMPIdentifiers(DMP dmp) {
        final List<Property> properties = new ArrayList<>();

        // DMP
        properties.addAll(propertyService.loadAllIdentifiers(dmp.getClassIdentifier(), "dmp_id", null));
        // Project
        properties.addAll(propertyService.loadAllIdentifiers(dmp.getClassIdentifier(), "project", "title"));
        // Funding
        properties.addAll(propertyService.loadAllIdentifiers(dmp.getClassIdentifier(), "funder_id", null));
        // Grant id
        properties.addAll(propertyService.loadAllIdentifiers(dmp.getClassIdentifier(), "grant_id", null));
        // Contact
        properties.addAll(propertyService.loadAllIdentifiers(dmp.getClassIdentifier(), "contact_id", null));
        // Contributor
        properties.addAll(propertyService.loadAllIdentifiers(dmp.getClassIdentifier(), "contributor_id", null));
        // Cost
        properties.addAll(propertyService.loadAllIdentifiers(dmp.getClassIdentifier(), "cost", "title"));
        // Dataset
        properties.addAll(propertyService.loadAllIdentifiers(dmp.getClassIdentifier(), "dataset_id", null));
        // Distribution
        properties.addAll(propertyService.loadAllIdentifiers(dmp.getClassIdentifier(), "distribution", "access_url"));

        return properties;
    }

}
