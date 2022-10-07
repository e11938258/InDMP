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
    private PropertyDao propertyDao;

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private RDMServiceDao rdmServiceDao;

    private final Logger log = LoggerFactory.getLogger(PropertyModule.class);

    /**
     * 
     * Create a new property
     * 
     * @param property
     * @param rdmService
     */
    public void persist(Property property, RDMService rdmService) {
        Objects.requireNonNull(property, "Property is null.");
        Objects.requireNonNull(rdmService, "Service is null.");

        // Add references to the RDM service
        property.getWasGeneratedBy().setWasStartedBy(rdmService);
        rdmService.addStartRelation(property.getWasGeneratedBy());

        // Persist a new activity
        activityDao.persist(property.getWasGeneratedBy());

        // Persist a new property
        propertyDao.persist(property);

        // Update the RDM service
        rdmServiceDao.update(rdmService);

        log.debug("Persisting a new property: " + property.toString());
    }

    /**
     *
     * Persist a list of properties
     *
     * @param properties
     * @param rdmService
     */
    @Transactional
    public void persistList(List<Property> properties, RDMService rdmService) {
        Objects.requireNonNull(properties, "List with properties is null.");
        // For each property
        for (Property property : properties) {
            persist(property, rdmService);
        }
    }

    /**
     * 
     * Find the property
     * 
     * @param atLocation
     * @param specializationOf
     * @param value
     * @param onlyActive
     * @return
     */
    @Transactional(readOnly = true)
    public Property findProperty(String atLocation, String specializationOf, String value, boolean onlyActive) {
        try {
            return propertyDao.findProperty(atLocation, specializationOf, value, onlyActive);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * 
     * Find the properties
     * 
     * @param atLocation
     * @param specializationOf
     * @param value
     * @param onlyActive
     * @return
     */
    @Transactional(readOnly = true)
    public List<Property> findProperties(String atLocation, String specializationOf, String value, boolean onlyActive) {
        return propertyDao.findProperties(atLocation, specializationOf, value, onlyActive);
    }

    /**
     * 
     * Find the properties with nested ones
     * 
     * @param atLocation
     * @param specializationOf
     * @param onlyActive
     * @return
     */
    @Transactional(readOnly = true)
    public List<Property> findAllProperties(String atLocation, String specializationOf, boolean onlyActive) {
        return propertyDao.findAllProperties(atLocation, specializationOf, onlyActive);
    }

    /**
     *
     * Terminate the current properties and create new ones
     *
     * @param properties
     * @param rdmService
     */
    @Transactional
    public void terminateAndCreateProperties(List<Property> properties, RDMService rdmService) {
        Objects.requireNonNull(properties, "List with properties is null.");
        // For each property
        for (Property property : properties) {
            terminateAndCreateProperty(property, rdmService);
        }
    }

    /**
     *
     * Terminate the current property and create new one
     *
     * @param property
     * @param rdmService
     */
    @Transactional
    public void terminateAndCreateProperty(Property property, RDMService rdmService) {
        Objects.requireNonNull(property, "Property is null.");
        Objects.requireNonNull(rdmService, "RDM service is null.");

        // Find the current property record
        final Property currentProperty = findProperty(property.getAtLocation(), property.getSpecializationOf(), null, true);

        // If the property exists
        if (currentProperty != null) {

            // If they do not have the same value
            if(!currentProperty.hasSameValue(property)) {
                // Terminate the original one
                terminate(currentProperty, property.getWasGeneratedBy().getStartedAtTime(), rdmService);

                // Persist a new property
                persist(property, rdmService);
            }
        } else {
            // Persist a new property
            persist(property, rdmService);
        }
    }

    /**
     *
     * Change atLocation for nested object properties
     *
     * @param originalAtLocation
     * @param newAtLocation
     */
    @Transactional
    public void changeNestedProperties(String originalAtLocation, String newAtLocation) {
        Objects.requireNonNull(originalAtLocation, "The original atlocation is null.");
        Objects.requireNonNull(originalAtLocation, "The new atLocation is null.");

        // Get all properties
        List<Property> properties = findAllProperties(originalAtLocation, null, false);

        // For each property update the atLocation
        for (final Property property : properties) {
            property.setAtLocation(property.getAtLocation().replace(originalAtLocation, newAtLocation));
            propertyDao.update(property);
            log.debug("Changing the location for " + property.toString());
        }
    }

    /**
     *
     * Terminate all properties
     *
     * @param atLocation
     * @param endTime
     * @param rdmService
     */
    @Transactional
    public void terminateAllProperties(String atLocation, LocalDateTime endTime, RDMService rdmService) {
        Objects.requireNonNull(atLocation, "The current atLocation is null.");
        Objects.requireNonNull(atLocation, "End time is null.");
        Objects.requireNonNull(rdmService, "RDM service is null.");

        // Find all properties
        List<Property> properties = findAllProperties(atLocation, null, true);

        // Terminate each found property
        for (final Property property : properties) {
            terminate(property, Timestamp.valueOf(endTime), rdmService);
        }
    }

    /* Private */

    @Transactional
    private void terminate(Property property, Timestamp endTime, RDMService rdmService) {
        Objects.requireNonNull(property, "The property is null.");
        Objects.requireNonNull(endTime, "The end time is null.");
        Objects.requireNonNull(rdmService, "The service is null.");

        final Activity currentActivity = property.getWasGeneratedBy();

        // Add references to the RDM service
        currentActivity.setWasEndedBy(rdmService);
        rdmService.addEndRelation(currentActivity);

        // End previous activity
        currentActivity.setEndedAtTime(endTime);

        // Update the activity
        activityDao.update(currentActivity);

        // Update the RDM service
        rdmServiceDao.update(rdmService);
    }
}
