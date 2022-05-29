package at.tuwien.indmp.service.impl;

import at.tuwien.indmp.dao.ActivityDao;
import at.tuwien.indmp.dao.EntityDao;
import at.tuwien.indmp.model.Activity;
import at.tuwien.indmp.model.DataService;
import at.tuwien.indmp.model.Entity;
import at.tuwien.indmp.service.DataServiceService;
import at.tuwien.indmp.service.EntityService;

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
public class EntityServiceImpl implements EntityService {

    @Autowired
    private EntityDao entityDao;

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private DataServiceService dataServiceService;

    private final Logger log = LoggerFactory.getLogger(EntityServiceImpl.class);

    /**
     *
     * Persist a list of entities
     *
     * @param entities
     * @param dataService
     */
    @Override
    @Transactional
    public void persist(List<Entity> entities, DataService dataService) {
        Objects.requireNonNull(entities, "List with entities is null.");
        // For each entity
        for (Entity entity : entities) {
            persist(entity, dataService);
        }
    }

    private void persist(Entity entity, DataService dataService) {
        Objects.requireNonNull(entity, "Entity is null.");
        Objects.requireNonNull(dataService, "Service is null.");

        log.info("Persisting a new entity: " + entity.toString());

        // Add references
        entity.getWasGeneratedBy().setWasAssociatedWith(dataService);
        dataService.add(entity.getWasGeneratedBy());
        // Persist the new activity
        activityDao.persist(entity.getWasGeneratedBy());
        // Persist the new entity
        entityDao.persist(entity);
        // Update the RDM service
        dataServiceService.update(dataService);
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
    @Override
    @Transactional(readOnly = true)
    public Entity findEntity(String atLocation, String specializationOf, String value) {
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
    @Override
    @Transactional(readOnly = true)
    public List<Entity> findEntities(String atLocation, String specializationOf, String value, boolean onlyActive) {
        return entityDao.findEntities(atLocation, specializationOf, value, onlyActive);
    }

    /**
     * 
     * Find entities with nested ones
     * 
     * @param atLocation
     * @param specializationOf
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<Entity> findAllEntities(String atLocation, String specializationOf, boolean onlyActive) {
        return entityDao.findAllEntities(atLocation, specializationOf, onlyActive);
    }

    /**
     *
     * Deactivate the current entities and create new ones
     *
     * @param entities
     * @param dataService
     */
    @Override
    @Transactional
    public void deactivateAndCreateEntities(List<Entity> entities, DataService dataService) {
        Objects.requireNonNull(entities, "List with entities is null.");

        // For each entity
        for (Entity entity : entities) {
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
    @Override
    @Transactional
    public void deactivateAndCreateEntity(Entity entity, DataService dataService) {
        Objects.requireNonNull(entity, "Entity is null.");
        Objects.requireNonNull(dataService, "Data service is null.");

        // Find current record
        final Entity currentEntity = findEntity(entity.getAtLocation(), entity.getSpecializationOf(), null);

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

    private void deactivate(Entity entity, Timestamp endTime) {
        Objects.requireNonNull(entity, "The current entity is null.");

        // End previous activity
        final Activity currentActivity = entity.getWasGeneratedBy();
        currentActivity.setEndedAtTime(endTime);
        activityDao.update(currentActivity);
    }

    /**
     *
     * Change atLocation after identifier change
     *
     * @param currentLocation
     * @param newLocation
     */
    @Override
    @Transactional
    public void changeNestedEntities(String currentLocation, String newLocation) {
        Objects.requireNonNull(currentLocation, "Current atlocation is null.");
        Objects.requireNonNull(currentLocation, "New atLocation is null.");

        // Change class identifiers
        List<Entity> entities = findAllEntities(currentLocation, null, false);
        // For each entity update location
        for (final Entity entity : entities) {
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
    @Override
    @Transactional
    public void removeAllNestedEntities(String currentLocation, LocalDateTime endTime) {
        Objects.requireNonNull(currentLocation, "Current location is null.");

        // Find all entities
        List<Entity> entities = findAllEntities(currentLocation, null, true);

        // Deactivate them
        for (final Entity entity : entities) {
            deactivate(entity, Timestamp.valueOf(endTime));
        }
    }
}
