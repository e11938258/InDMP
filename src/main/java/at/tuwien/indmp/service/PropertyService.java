package at.tuwien.indmp.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import at.tuwien.indmp.dao.PropertyDao;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.System;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertyService {

    @Autowired
    private PropertyDao propertyDao;

    private final Logger log = LoggerFactory.getLogger(PropertyService.class);

    /**
     *
     * Persist a new property
     *
     * @param property
     */
    @Transactional
    public void persist(Property property) {
        Objects.requireNonNull(property, "Property is null.");
        log.info("Persisting a new property: " + property.toString());
        propertyDao.persist(property);
    }

    /**
     *
     * Persist a list of properties
     *
     * @param properties
     */
    @Transactional
    public void persist(List<Property> properties) {
        Objects.requireNonNull(properties, "List with properties is null.");
        // For each property
        for (Property property : properties) {
            persist(property);
        }
    }

    /**
     * 
     * Find property
     * 
     * @param dmpIdentifier
     * @param className
     * @param classIdentifier
     * @param propertyName
     * @param reference
     * @param value
     * @return
     */
    @Transactional(readOnly = true)
    public Property findProperty(String dmpIdentifier, String className, String classIdentifier, String propertyName,
            String value, String reference) {
        List<Property> properties = propertyDao.findProperty(dmpIdentifier, className, classIdentifier,
                propertyName, value, reference);
        if (properties.size() == 1) {
            return properties.get(0);
        } else {
            return null;
        }
    }

    /**
     * Find property
     * 
     * @param property
     * @return
     */
    @Transactional(readOnly = true)
    public Property findProperty(Property property) {
        Objects.requireNonNull(property, "Property is null.");
        return findProperty(property.getDmpIdentifier(), property.getClassName(), property.getClassIdentifier(),
                property.getPropertyName(), property.getValue(), property.getReference());
    }

    /**
     * 
     * Find properties
     * 
     * @param dmpIdentifier
     * @param className
     * @param classIdentifier
     * @param propertyName
     * @param reference
     * @return
     */
    @Transactional(readOnly = true)
    public List<Property> findProperties(String dmpIdentifier, String className, String classIdentifier,
            String propertyName, String value, String reference) {
        return propertyDao.findProperty(dmpIdentifier, className, classIdentifier,
                propertyName, value, reference);
    }

    /**
     * Find properties
     * 
     * @param property
     * @return
     */
    @Transactional(readOnly = true)
    public List<Property> findProperties(Property property) {
        Objects.requireNonNull(property, "Property is null.");
        return findProperties(property.getDmpIdentifier(), property.getClassName(), property.getClassIdentifier(),
                property.getPropertyName(), property.getValue(), property.getReference());
    }

    /**
     * 
     * Load all identifiers of class
     * 
     * @param dmpIdentifier
     * @param className
     * @param propertyName
     * @return
     */
    public List<Property> loadAllIdentifiers(String dmpIdentifier, String className, String propertyName) {
        return propertyDao.findIdentifiers(dmpIdentifier, className, propertyName);
    }

    /**
     * 
     * Set new property
     * 
     * @param property
     */
    @Transactional
    public void setNewProperty(Property property) {
        Objects.requireNonNull(property, "Property is null.");
        // Find old record if exists
        final Property oldProperty = findProperty(property.getDmpIdentifier(), property.getClassName(),
                property.getClassIdentifier(), property.getPropertyName(), null, property.getReference());
        if (oldProperty != null) {
            // If values are different, set date until and persits new value
            if (!oldProperty.hasSameValue(property)) {
                oldProperty.setValidUntil(property.getValidFrom());
                propertyDao.update(oldProperty);
                // Persist new property
                persist(property);
            }
        } else {
            // Persist new property
            persist(property);
        }
    }

    /**
     * 
     * Set new properties
     * 
     * @param properties
     */
    @Transactional
    public void setNewProperties(List<Property> properties) {
        Objects.requireNonNull(properties, "List with properties is null.");
        // For each property
        for (Property property : properties) {
            setNewProperty(property);
        }
    }

    /**
     * 
     * Set new property with value
     * 
     * @param properties
     */
    @Transactional
    public void setNewPropertyWithValue(Property currentProperty, String newValue, Date validFrom, System system) {
        Objects.requireNonNull(currentProperty, "Current property is null.");
        Objects.requireNonNull(newValue, "New value is null.");
        Objects.requireNonNull(validFrom, "valid_from is null.");
        // Create a new property
        final Property property = new Property(currentProperty, newValue, validFrom, system);
        setNewProperty(property);
        // Add new property to system
        system.add(property);
    }

    /**
     * 
     * End the life of the property
     * 
     * @param property
     * @param validUntil
     */
    @Transactional
    public void endPropertyLife(Property property, Date validUntil) {
        Objects.requireNonNull(property, "Property is null.");
        Objects.requireNonNull(validUntil, "valid_until is null.");

        final Property p = findProperty(property);
        if (p != null) {
            p.setValidUntil(validUntil);
            propertyDao.update(p);
        }
    }

    /**
     * 
     * End the life of the properties
     * 
     * @param properties
     * @param validUntil
     */
    @Transactional
    public void endPropertyLife(List<Property> properties, Date validUntil) {
        Objects.requireNonNull(properties, "Properties is null.");
        Objects.requireNonNull(validUntil, "valid_until is null.");

        // For each property
        for (Property property : properties) {
            endPropertyLife(property, validUntil);
        }
    }

    /**
     * 
     * End all nested properties
     * 
     * @param DMPIdentifier
     * @param validUntil
     * @param identifier
     */
    @Transactional
    public void endAllNestedProperties(String DMPIdentifier, Date validUntil, String identifier) {
        Objects.requireNonNull(DMPIdentifier, "DMP identifier is null.");
        Objects.requireNonNull(identifier, "Identifier is null.");

        // TODO: Limit it to specific classes, now it ends everything what has the
        // class identifier/reference

        // End all nested properties
        List<Property> properties = findProperties(DMPIdentifier, null, null, null, null, identifier);
        for (final Property property : properties) {
            if(!identifier.equals(property.getClassIdentifier())) {
                endAllNestedProperties(DMPIdentifier, validUntil, property.getClassIdentifier());
            }
        }

        // End all class properties
        endPropertyLife(findProperties(DMPIdentifier, null, identifier, null, null, null), validUntil);
    }

    /**
     * 
     * Change class identifiers and references of nested instances
     * 
     * @param DMPIdentifier
     * @param identifier
     * @param newIdentifier
     */
    @Transactional
    public void changeNestedIdentifiers(String DMPIdentifier, String identifier, String newIdentifier) {
        Objects.requireNonNull(DMPIdentifier, "DMP identifier is null.");
        Objects.requireNonNull(identifier, "Identifier is null.");
        Objects.requireNonNull(newIdentifier, "New identifier is null.");

        // TODO: Limit it to specific classes, now its upgrading everything what has the
        // reference/class identifier

        // Change class identifiers
        List<Property> properties = findProperties(DMPIdentifier, null, identifier, null, null, null);
        // For each property update class identifier
        for (final Property property : properties) {
            log.info("Changing the class identifier for " + property.toString());
            property.setClassIdentifier(newIdentifier);
            propertyDao.update(property);
        }

        // Change references
        properties = findProperties(DMPIdentifier, null, null, null, null, identifier);
        // For each property update reference
        for (final Property property : properties) {
            log.info("Changing the reference for " + property.toString());
            property.setReference(newIdentifier);
            propertyDao.update(property);
        }
    }
}
