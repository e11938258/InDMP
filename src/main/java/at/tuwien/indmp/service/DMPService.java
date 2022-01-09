package at.tuwien.indmp.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import at.tuwien.indmp.exception.NotFoundException;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.System;
import at.tuwien.indmp.model.dmp.DMP;
import at.tuwien.indmp.model.dmp.DMPScheme;
import at.tuwien.indmp.model.dmp.Dmp_id;
import at.tuwien.indmp.model.idmp.ElementIdentifier;
import at.tuwien.indmp.model.idmp.Identifier;
import at.tuwien.indmp.util.dmp.DataIdentifierType;
import at.tuwien.indmp.util.idmp.ClassType;

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
    SystemService systemService;

    private final Logger log = LoggerFactory.getLogger(DMPService.class);

    public static Map<ClassType, String[]> identifierChangeableClasses;
    static {
        identifierChangeableClasses = new HashMap<>();
        identifierChangeableClasses.put(ClassType.dmp, new String[] { "dmp_id", "identifier", "type" });
        identifierChangeableClasses.put(ClassType.contributor, new String[] { "contributor_id", "identifier", "type" });
        identifierChangeableClasses.put(ClassType.cost, new String[] { "cost", "title" });
        identifierChangeableClasses.put(ClassType.project, new String[] { "project", "title" });
        identifierChangeableClasses.put(ClassType.funding, new String[] { "funder_id", "identifier", "type" });
        identifierChangeableClasses.put(ClassType.dataset, new String[] { "dataset_id", "identifier", "type" });
        identifierChangeableClasses.put(ClassType.distribution, new String[] { "distribution", "access_url" });
        identifierChangeableClasses.put(ClassType.grant_id, new String[] { "grant_id", "identifier", "type" });
    }

    /**
     * 
     * Identify DMP by creation date and identifier
     * 
     * @param dmp
     * @param system
     * @return minimum of DMP
     */
    public DMP identifyDMP(DMP dmp, System system) {
        Objects.requireNonNull(dmp.getCreated(), "Creation date is null.");
        Objects.requireNonNull(dmp.getModified(), "Modification date is null.");
        Objects.requireNonNull(dmp.getDmp_id(), "DMP ID is null.");
        Objects.requireNonNull(dmp.getDmp_id().getClassIdentifier(), "DMP identifier is null.");
        
        DMP currentDMP = null;
        // Is creation date same as modification date?
        if (dmp.getCreated().equals(dmp.getModified())) {
            return null;
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
                        List<Identifier> identifier = new ArrayList<>();
                        identifier.add(new Identifier(ClassType.dmp,
                                new ElementIdentifier(currentDMP.getDmp_id().getIdentifier(),
                                        currentDMP.getDmp_id().getType().toString()),
                                new ElementIdentifier(dmp.getDmp_id().getIdentifier(),
                                        dmp.getDmp_id().getType().toString())));
                        changeIdentifiers(currentDMP, identifier, dmp.getModified(), system);
                    }
                    // Identification by creation only?
                    if (byCreationOnly) {
                        return currentDMP;
                    } else {
                        log.error(
                                "Cannot find dmp by identifier, creation date: " + currentDMP.getCreated().toString());
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

    /**
     * 
     * Find dmp by creation date
     * 
     * @param creationDate
     * @return
     */
    public DMP findByCreationDate(String creationDate) {
        Property property = propertyService.findProperty(null, "dmp", null, "created", creationDate, null);
        if (property != null) {
            return loadMinimalDMP(property.getDmpIdentifier());
        } else {
            return null;
        }
    }

    /**
     * 
     * Find dmp by identifier
     * 
     * @param identifier
     * @return
     */
    private DMP findByIdentifier(String identifier) {
        Property property = propertyService.findProperty(null, "dmp_id", null, "identifier", identifier, null);
        if (property != null) {
            return loadMinimalDMP(identifier);
        } else {
            return null;
        }
    }

    private DMP loadMinimalDMP(String dmpIdentifier) {
        final String created = propertyService.findProperty(dmpIdentifier, "dmp", null, "created", null, null)
                .getValue();
        final String modified = propertyService.findProperty(dmpIdentifier, "dmp", null, "modified", null, null)
                .getValue();
        final String identifier = propertyService.findProperty(dmpIdentifier, "dmp_id", null, "identifier", null, null)
                .getValue();
        final String type = propertyService.findProperty(dmpIdentifier, "dmp_id", null, "type", null, null).getValue();
        final Dmp_id dmp_id = new Dmp_id(identifier, DataIdentifierType.valueOf(type));
        try {
            return new DMP(DMP.DATE_FORMATTER.parse(created), DMP.DATE_FORMATTER.parse(modified), dmp_id);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 
     * Change identifiers
     * 
     * @param currentDMP
     * @param identifiers
     * @param modified
     * @param system
     */
    public void changeIdentifiers(DMP currentDMP, List<Identifier> identifiers, Date modified,
            System system) {
        Objects.requireNonNull(currentDMP, "DMP is null.");
        Objects.requireNonNull(identifiers, "Identifier array is null.");
        Objects.requireNonNull(system, "System is null.");

        // For each identifier
        for (Identifier identifier : identifiers) {
            if (identifierChangeableClasses.containsKey(identifier.getType())) {
                // Get data about identifier
                final String[] identifierData = identifierChangeableClasses.get(identifier.getType());
                // Find property
                final Property currentIdentifier = propertyService.findProperty(currentDMP.getClassIdentifier(),
                        identifierData[0], identifier.getId().getIdentifier(), identifierData[1],
                        identifier.getId().getIdentifier(), null);
                Property currentType = null;
                // Has identifier type?
                if (identifierData.length > 2) {
                    currentType = propertyService.findProperty(currentDMP.getClassIdentifier(),
                            identifierData[0], identifier.getId().getIdentifier(), identifierData[2],
                            identifier.getId().getType(), null);
                }

                // Found?
                if (currentIdentifier != null && (identifierData.length == 2 || currentType != null)) {
                    // Set new properties
                    propertyService.setNewPropertyWithValue(currentIdentifier, identifier.getNew_id().getIdentifier(),
                            modified, system);
                    if (currentType != null) {
                        propertyService.setNewPropertyWithValue(currentType, identifier.getNew_id().getType(),
                                modified, system);
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
        // Update system
        systemService.update(system);
    }

    /**
     * 
     * Delete instances
     * 
     * @param currentDMP
     * @param identifiers
     * @param system
     */
    public void deleteInstances(DMP currentDMP, List<Identifier> identifiers) {
        Objects.requireNonNull(currentDMP, "DMP is null.");
        Objects.requireNonNull(identifiers, "Identifier array is null.");

        // For each identifier find instance and end properties
        for (Identifier identifier : identifiers) {
            propertyService.endAllNestedProperties(currentDMP.getClassIdentifier(), currentDMP.getModified(),
                    identifier.getId().getIdentifier());
        }
    }

    /**
     * 
     * Create a new DMP
     * 
     * @param dmp
     */
    public void create(DMP dmp, System system) {
        // Get properties from new DMP
        final List<Property> properties = dmp.getProperties(dmp, null, system);
        properties.addAll(dmp.getPropertiesFromNestedClasses(dmp, system));
        // Persist the properties
        propertyService.persist(properties);
        // Update system
        systemService.update(system);
    }

    /**
     * Update dmp
     * 
     * @param currentDMP
     * @param dmp
     */
    public void update(DMP currentDMP, DMP dmp, System system) {
        // Get properties from new DMP
        final List<Property> properties = dmp.getProperties(dmp, null, system);
        properties.addAll(dmp.getPropertiesFromNestedClasses(dmp, system));
        // Set new properties
        propertyService.setNewProperties(properties);
        // Update system
        systemService.update(system);

    }

    /**
     * 
     * Update the property modified
     * 
     * @param currentDMP
     * @param modified
     * @param system
     */
    public void updateModified(DMP currentDMP, Date modified, System system) {
        // Find property
        final Property currrentProperty = propertyService.findProperty(currentDMP.getClassIdentifier(), "dmp", null,
                "modified", null, null);
        // Set new property
        propertyService.setNewPropertyWithValue(currrentProperty, DMP.DATE_FORMATTER.format(modified), modified,
                system);
        // Update system
        systemService.update(system);
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
