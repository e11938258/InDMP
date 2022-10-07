package at.tuwien.indmp.modul;

import at.tuwien.indmp.dao.ActivityDao;
import at.tuwien.indmp.dao.PropertyDao;
import at.tuwien.indmp.dao.RDMServiceDao;
import at.tuwien.indmp.model.Activity;
import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.model.Property;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertyModule {

    @Autowired
    private PropertyDao entityDao;

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private RDMServiceDao rdmServiceDao;

    private final Logger log = LoggerFactory.getLogger(PropertyModule.class);

    /**
     *
     * Persist a list of entities
     *
     * @param entities
     * @param dataService
     */
    @Transactional
    public void persist(List<Property> entities, RDMService dataService) {
        Objects.requireNonNull(entities, "List with entities is null.");
        // For each entity
        for (Property entity : entities) {
            persist(entity, dataService);
        }
    }

    /**
     * 
     * Find entity
     * 
     * @param atLocation
     * @param specializationOf
     * @param value
     * @return
     */
    @Transactional(readOnly = true)
    public Property findEntity(String atLocation, String specializationOf, String value) {
        try {
            return entityDao.findEntity(atLocation, specializationOf, value);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * 
     * Find entities
     * 
     * @param atLocation
     * @param specializationOf
     * @return
     */
    @Transactional(readOnly = true)
    public List<Property> findEntities(String atLocation, String specializationOf, String value, boolean onlyActive) {
        return entityDao.findProperties(atLocation, specializationOf, value, onlyActive);
    }

    /**
     * 
     * Find entities with nested ones
     * 
     * @param atLocation
     * @param specializationOf
     * @return
     */
    @Transactional(readOnly = true)
    public List<Property> findAllEntities(String atLocation, String specializationOf, boolean onlyActive) {
        return entityDao.findAllProperties(atLocation, specializationOf, onlyActive);
    }

    /**
     *
     * Deactivate the current entities and create new ones
     *
     * @param entities
     * @param dataService
     */
    @Transactional
    public void deactivateAndCreateEntities(List<Property> entities, RDMService dataService) {
        Objects.requireNonNull(entities, "List with entities is null.");

        // For each entity
        for (Property entity : entities) {
            deactivateAndCreateEntity(entity, dataService);
        }
    }

    /**
     *
     * Deactivate the current entity and create new one
     *
     * @param entities
     * @param dataService
     */
    @Transactional
    public void deactivateAndCreateEntity(Property entity, RDMService dataService) {
        Objects.requireNonNull(entity, "Entity is null.");
        Objects.requireNonNull(dataService, "Data service is null.");

        // Find current record
        final Property currentEntity = findEntity(entity.getAtLocation(), entity.getSpecializationOf(), null);

        // If entity exists
        if (currentEntity != null) {
            // If they do not have the same value
            if(!currentEntity.hasSameValue(entity)) {
                // Deactivate the old one
                deactivate(currentEntity, entity.getWasGeneratedBy().getStartedAtTime());
                // Persist new entity
                persist(entity, dataService);
            }
        } else {
            // Persist new entity
            persist(entity, dataService);
        }
    }

    /**
     *
     * Change atLocation after identifier change
     *
     * @param currentLocation
     * @param newLocation
     */
    @Transactional
    public void changeNestedEntities(String currentLocation, String newLocation) {
        Objects.requireNonNull(currentLocation, "Current atlocation is null.");
        Objects.requireNonNull(currentLocation, "New atLocation is null.");

        // Change class identifiers
        List<Property> entities = findAllEntities(currentLocation, null, false);
        // For each entity update location
        for (final Property entity : entities) {
            log.info("Changing the location for " + entity.toString());
            entity.setAtLocation(entity.getAtLocation().replace(currentLocation, newLocation));
            entityDao.update(entity);
        }
    }

    /**
     *
     * Remove all nested entites
     *
     * @param currentLocation
     */
    @Transactional
    public void removeAllNestedEntities(String currentLocation, LocalDateTime endTime) {
        Objects.requireNonNull(currentLocation, "Current location is null.");

        // Find all entities
        List<Property> entities = findAllEntities(currentLocation, null, true);

        // Deactivate them
        for (final Property entity : entities) {
            deactivate(entity, Timestamp.valueOf(endTime));
        }
    }

    /* Private */

    private void persist(Property entity, RDMService dataService) {
        Objects.requireNonNull(entity, "Entity is null.");
        Objects.requireNonNull(dataService, "Service is null.");

        log.info("Persisting a new entity: " + entity.toString());

        // Add references
        entity.getWasGeneratedBy().setWasStartedBy(dataService);
        dataService.addStartRelation(entity.getWasGeneratedBy());
        // Persist the new activity
        activityDao.persist(entity.getWasGeneratedBy());
        // Persist the new entity
        entityDao.persist(entity);
        // Update the RDM service
        rdmServiceDao.update(dataService);
    }

    private void deactivate(Property entity, Timestamp endTime) {
        Objects.requireNonNull(entity, "The current entity is null.");

        // End previous activity
        final Activity currentActivity = entity.getWasGeneratedBy();
        currentActivity.setEndedAtTime(endTime);
        activityDao.update(currentActivity);
    }
}
