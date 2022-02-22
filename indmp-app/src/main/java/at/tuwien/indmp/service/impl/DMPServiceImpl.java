package at.tuwien.indmp.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import at.tuwien.indmp.exception.BadRequestException;
import at.tuwien.indmp.exception.ForbiddenException;
import at.tuwien.indmp.exception.NotFoundException;
import at.tuwien.indmp.model.DataService;
import at.tuwien.indmp.model.Entity;
import at.tuwien.indmp.model.dmp.DMP;
import at.tuwien.indmp.model.dmp.DMPScheme;
import at.tuwien.indmp.model.dmp.DMP_id;
import at.tuwien.indmp.service.DMPService;
import at.tuwien.indmp.service.EntityService;
import at.tuwien.indmp.util.ModelConstants;
import at.tuwien.indmp.util.Functions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DMPServiceImpl implements DMPService {

    @Value("${identification.by-creation-only}")
    private boolean byCreationOnly;

    @Value("${identification.auto-correction}")
    private boolean autoCorrection;

    @Value("${identification.by-identifier-only}")
    private boolean byIdentifierOnly;

    @Autowired
    EntityService entityService;

    private final Logger log = LoggerFactory.getLogger(DMPServiceImpl.class);

    /**
     * 
     * Create a new DMP
     * 
     * @param dmp
     * @param dataService
     */
    public void create(DMP dmp, DataService dataService) {
        // Get properties from new DMP
        final List<Entity> properties = dmp.getProperties(dmp, "", dataService);
        properties.addAll(dmp.getPropertiesFromNestedClasses(dmp, "", dataService));

        // Persist the properties
        entityService.persist(properties, dataService);
    }

    /**
     * 
     * Identify DMP by creation date and identifier
     * 
     * @param dmp
     * @param dataService
     * @return minimum of DMP
     */
    public DMP identifyDMP(@Valid DMP dmp, DataService dataService) {
        // Check minimal DMP
        checkMinimalDMP(dmp);
        // Is creation date same as modification date?
        DMP currentDMP = null;
        if (dmp.isNew()) {
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
                    if (autoCorrection && dataService != null) {
                        // List<IdentifierUnit> identifier = new ArrayList<>();
                        // identifier.add(new IdentifierUnit("dmp",
                        // new Identifier(currentDMP.getDmp_id().getIdentifier(),
                        // currentDMP.getDmp_id().getType().toString()),
                        // new Identifier(dmp.getDmp_id().getIdentifier(),
                        // dmp.getDmp_id().getType().toString())));
                        // changeIdentifiers(currentDMP, identifier, dmp.getModified(), dataService);
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
        final Entity entity = entityService.findEntity(null, "dmp:created", creationDate);
        if (entity != null) {
            return loadMinimalDMP(entity.getAtLocation());
        } else {
            return null;
        }
    }

    private DMP findByIdentifier(String identifier) {
        final Entity entity = entityService.findEntity(null, "dmp:identifier", identifier);
        if (entity != null) {
            return loadMinimalDMP(entity.getAtLocation());
        } else {
            return null;
        }
    }

    private DMP loadMinimalDMP(String atLocation) {
        // Find mandatory properties
        final List<Entity> properties = entityService.findEntities(atLocation, null);
        final String created = Functions.findPropertyInList("dmp", "created", properties).getValue();
        final String modified = Functions.findPropertyInList("dmp", "modified", properties).getValue();
        final String identifier = Functions.findPropertyInList("dmp", "identifier", properties).getValue();
        final String type = Functions.findPropertyInList("dmp", "type", properties).getValue();

        // Create a new minimal DMP
        final DMP_id dmp_id = new DMP_id(identifier, type);
        try {
            return new DMP(ModelConstants.DATE_TIME_FORMATTER_ISO_8601.parse(created),
                    ModelConstants.DATE_TIME_FORMATTER_ISO_8601.parse(modified), dmp_id);
        } catch (ParseException e) {
            return null;
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
        wholeDMP.build(entityService, dmp.getLocation(""));

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
    public void update(DMP dmp, DataService dataService) {
        // Get properties from new DMP
        final List<Entity> properties = dmp.getProperties(dmp, "", dataService);
        properties.addAll(dmp.getPropertiesFromNestedClasses(dmp, "", dataService));

        // Set new properties
        entityService.updateOrCreateEntities(properties, dataService);
    }

    /**
     *
     * Change identifier
     *
     * @param dmp
     * @param identifiers
     * @param dataService
     */
    public void changeIdentifiers(DMP dmp, Entity identifier, DataService dataService) {
        Objects.requireNonNull(dmp, "DMP is null.");
        Objects.requireNonNull(identifier, "Identifier is null.");
        Objects.requireNonNull(dataService, "Service is null.");

        // Is the identifier changeable for the this class?
        if (ModelConstants.IDENTIFIER_CHANGEABLE_CLASSES.contains(identifier.getSpecializationOf())) {

            // Find identifier
            final Entity currentIdentifier = entityService.findEntity(identifier.getAtLocation(),
                    identifier.getSpecializationOf(), null);

            // Found?
            if (currentIdentifier != null) {
                // Change modified
                updateModified(dmp, dataService);

                // Create a new location
                final String oldLocation = currentIdentifier.getAtLocation();
                final String location = oldLocation.replace(currentIdentifier.getValue(), identifier.getValue());
                // Create a new entity
                entityService.update(
                        Functions.createEntity(dmp, oldLocation, currentIdentifier.getSpecializationOf(),
                                identifier.getValue()),
                        currentIdentifier, dataService);

                // Change nested locations
                entityService.changeNestedEntities(oldLocation, location);
            } else {
                log.error("Cannot find identifier at location " + identifier.getAtLocation());
                throw new NotFoundException("Cannot find identifier at location " + identifier.getAtLocation());
            }
        } else {
            log.error("Cannot change " + identifier.getSpecializationOf());
            throw new ForbiddenException("Cannot change " + identifier.getSpecializationOf());
        }
    }

    private void updateModified(DMP dmp, DataService dataService) {
        entityService.update(Functions.findPropertyInList("dmp", "modified", dmp.getProperties(dmp, "", dataService)),
                null, dataService);
    }

    /**
     *
     * Delete instance
     *
     * @param dmp
     * @param identifiers
     * @param system
     */
    public void deleteInstance(Entity entity) {
        Objects.requireNonNull(entity, "Entity is null.");
        entityService.removeAllNestedEntities(entity.getAtLocation());
    }

    /**
     *
     * Load DMP identifiers with history
     *
     * @param dmp
     * @return
     */
    public List<Entity> loadIdentifierHistory(DMP dmp) {
        final List<Entity> entities = new ArrayList<>();

        // // DMP
        // properties.addAll(propertyService.loadAllIdentifiers(dmp.getClassIdentifier(),
        //         "dmp_id", null));
        // // Project
        // properties.addAll(propertyService.loadAllIdentifiers(dmp.getClassIdentifier(),
        //         "project", "title"));
        // // Funding
        // properties.addAll(propertyService.loadAllIdentifiers(dmp.getClassIdentifier(),
        //         "funder_id", null));
        // // Grant id
        // properties.addAll(propertyService.loadAllIdentifiers(dmp.getClassIdentifier(),
        //         "grant_id", null));
        // // Contact
        // properties.addAll(propertyService.loadAllIdentifiers(dmp.getClassIdentifier(),
        //         "contact_id", null));
        // // Contributor
        // properties.addAll(propertyService.loadAllIdentifiers(dmp.getClassIdentifier(),
        //         "contributor_id", null));
        // // Cost
        // properties.addAll(propertyService.loadAllIdentifiers(dmp.getClassIdentifier(),
        //         "cost", "title"));
        // // Dataset
        // properties.addAll(propertyService.loadAllIdentifiers(dmp.getClassIdentifier(),
        //         "dataset_id", null));
        // // Distribution
        // properties.addAll(propertyService.loadAllIdentifiers(dmp.getClassIdentifier(),
        //         "distribution", "access_url"));

        return entities;
    }

}
