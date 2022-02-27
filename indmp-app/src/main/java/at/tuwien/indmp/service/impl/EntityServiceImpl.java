package at.tuwien.indmp.service.impl;

import at.tuwien.indmp.dao.ActivityDao;
import at.tuwien.indmp.dao.EntityDao;
import at.tuwien.indmp.model.Activity;
import at.tuwien.indmp.model.DataService;
import at.tuwien.indmp.model.Entity;
import at.tuwien.indmp.service.DataServiceService;
import at.tuwien.indmp.service.EntityService;

import java.util.ArrayList;
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

        // Add references
        entity.getWasGeneratedBy().setWasAssociatedWith(dataService);
        dataService.add(entity.getWasGeneratedBy());

        log.info("Persisting a new entity: " + entity.toString());

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
     * Find entities (using equal)
     * 
     * @param atLocation
     * @param specializationOf
     * @return
     */
    @Transactional(readOnly = true)
    public List<Entity> findEntities(String atLocation, String specializationOf) {
        return entityDao.findEntities(atLocation, specializationOf);
    }

    /**
     * 
     * Find all entities (with nested, using like)
     * 
     * @param atLocation
     * @param specializationOf
     * @return
     */
    @Transactional(readOnly = true)
    public List<Entity> findAllEntities(String atLocation, String specializationOf) {
        return entityDao.findAllEntities(atLocation, specializationOf);
    }

    /**
     *
     * Update or create new entities
     *
     * @param entities
     * @param dataService
     */
    @Transactional
    public void updateOrCreateEntities(List<Entity> entities, DataService dataService) {
        Objects.requireNonNull(entities, "List with entities is null.");

        // For each entity
        for (Entity entity : entities) {
            updateOrCreateEntity(entity, dataService);
        }
    }

    private void updateOrCreateEntity(Entity entity, DataService dataService) {
        Objects.requireNonNull(entity, "Entity is null.");
        Objects.requireNonNull(dataService, "Data service is null.");

        // Find current record
        final Entity currentEntity = findEntity(entity.getAtLocation(), entity.getSpecializationOf(), null);

        // If entity exists
        if (currentEntity != null) {
            update(entity, currentEntity, dataService);
        } else {
            // Persist new property
            persist(entity, dataService);
        }
    }

    /**
     *
     * Update entity
     *
     * @param entity
     * @param currentEntity can be null
     * @param dataService
     */
    @Transactional
    public void update(Entity entity, Entity currentEntity, DataService dataService) {
        Objects.requireNonNull(entity, "Entity is null.");
        Objects.requireNonNull(dataService, "Data service is null.");

        // Find current record if not set
        if (currentEntity == null) {
            currentEntity = findEntity(entity.getAtLocation(), entity.getSpecializationOf(), null);
        }

        // If entity exists and values are different
        if (currentEntity != null && !currentEntity.hasSameValue(entity)) {
            // Set the new value
            currentEntity.setValue(entity.getValue());

            // End previous activity
            final Activity currentActivity = currentEntity.getWasGeneratedBy();
            currentActivity.setEndedAtTime(entity.getWasGeneratedBy().getStartedAtTime());
            currentActivity.setGenerated(null);

            // Change activity reference
            currentEntity.setWasGeneratedBy(entity.getWasGeneratedBy());
            entity.getWasGeneratedBy().setGenerated(currentEntity);

            // Add references to data service
            currentEntity.getWasGeneratedBy().setWasAssociatedWith(dataService);
            dataService.add(currentEntity.getWasGeneratedBy());

            // Persist a new activity
            activityDao.persist(currentEntity.getWasGeneratedBy());

            // Update entity
            entityDao.update(currentEntity);
            activityDao.update(currentActivity);
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
        List<Entity> entities = findAllEntities(currentLocation, null);
        // For each property update location
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
    @Transactional
    public void removeAllNestedEntities(String currentLocation) {
        Objects.requireNonNull(currentLocation, "Current location is null.");

        List<Entity> entities = findAllEntities(currentLocation, null);
        for (final Entity entity : entities) {
            entityDao.delete(entity);
        }
    }

    /**
     *
     * Load identifier history of class
     *
     * @param location
     * @param specializationOf
     * @return
     */
    public List<Entity> loadIdentifierHistory(String location, String specializationOf) {
        Objects.requireNonNull(location);
        Objects.requireNonNull(specializationOf);

        final List<Entity> entities = new ArrayList<>();

        // Find current instance
        for(Entity entity: entityDao.findAllEntities(location, specializationOf)) {
            // Add current one to list
            entities.add(entity);

            // Add history
            entities.addAll(entityDao.findInHistory(entity.getId()));
        }

        return entities;
    }

}
