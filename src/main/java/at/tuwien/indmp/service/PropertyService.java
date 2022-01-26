package at.tuwien.indmp.service;

import java.util.List;
import java.util.Objects;

import at.tuwien.indmp.dao.PropertyDao;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.RDMService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PropertyService {

    @Autowired
    private PropertyDao propertyDao;

    @Autowired
    private RDMServiceLayer rdmServiceLayer;

    private final Logger log = LoggerFactory.getLogger(PropertyService.class);

    /**
     *
     * Persist a new property
     *
     * @param property
     * @param rdmService
     */
    @Transactional
    public void persist(Property property, RDMService rdmService) {
        Objects.requireNonNull(property, "Property is null.");
        Objects.requireNonNull(rdmService, "RDM service is null.");

        // Add references
        property.setRDMService(rdmService);
        rdmService.add(property);

        // Persist the new property
        propertyDao.persist(property);

        // Update the RDM service
        rdmServiceLayer.update(rdmService);

        log.info("Persisting a new property: " + property.toString());
    }

    /**
     *
     * Persist a list of properties
     *
     * @param properties
     * @param rdmService
     */
    @Transactional
    public void persist(List<Property> properties, RDMService rdmService) {
        Objects.requireNonNull(properties, "List with properties is null.");
        // For each property
        for (Property property : properties) {
            persist(property, rdmService);
        }
    }

    /**
     * 
     * Find property
     * 
     * @param dmpIdentifier
     * @param classType
     * @param classIdentifier
     * @param propertyName
     * @param reference
     * @param value
     * @return
     */
    @Transactional(readOnly = true)
    public Property find(String dmpIdentifier, String classType, String classIdentifier, String propertyName,
            String value, String reference) {
        List<Property> properties = propertyDao.findProperty(dmpIdentifier, classType, classIdentifier,
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
    public Property find(Property property) {
        Objects.requireNonNull(property, "Property is null.");
        return find(property.getDmpIdentifier(), property.getClassType(), property.getClassIdentifier(),
                property.getPropertyName(), property.getValue(), property.getReference());
    }

    /**
     * 
     * Find properties
     * 
     * @param dmpIdentifier
     * @param classType
     * @param classIdentifier
     * @param propertyName
     * @param reference
     * @return
     */
    @Transactional(readOnly = true)
    public List<Property> findProperties(String dmpIdentifier, String classType, String classIdentifier,
            String propertyName, String value, String reference) {
        return propertyDao.findProperty(dmpIdentifier, classType, classIdentifier,
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
        return findProperties(property.getDmpIdentifier(), property.getClassType(), property.getClassIdentifier(),
                property.getPropertyName(), property.getValue(), property.getReference());
    }

    /**
     * 
     * Update the property
     * 
     * @param property
     * @param rdmService
     */
    @Transactional
    public void update(Property property, RDMService rdmService) {
        Objects.requireNonNull(property, "Property is null.");

        // Set new RDM service
        if (!property.getRDMService().equals(rdmService)) {
            // Get old RDM system
            final RDMService currentRDMService = property.getRDMService();

            // Set new system
            currentRDMService.remove(property);
            rdmService.add(property);
            property.setRDMService(rdmService);

            // Update RDM services
            rdmServiceLayer.update(currentRDMService);
            rdmServiceLayer.update(rdmService);
        }

        // Update property
        propertyDao.update(property);
    }

    /**
     * 
     * Load and update the property
     * 
     * @param dmpIdentifier
     * @param classType
     * @param classIdentifier
     * @param propertyName
     * @param newValue
     * @param reference
     * @param rdmService
     * @return
     */
    public void loadAndUpdate(String dmpIdentifier, String classType, String classIdentifier, String propertyName,
            String newValue, String reference, RDMService rdmService) {
        // Find current property
        final Property property = find(dmpIdentifier, classType, classIdentifier, propertyName,
                null, reference);
        
        // Set new value
        if (property != null) {
            property.setValue(newValue);
            update(property, rdmService);
        }
    }

    /**
     * 
     * Update the property if exists, otherwise create a new one
     * 
     * @param newProperty
     * @param rdmService
     */
    @Transactional
    public void updateOrCreateProperty(Property newProperty, RDMService rdmService) {
        Objects.requireNonNull(newProperty, "Property is null.");
        Objects.requireNonNull(rdmService, "RDM service is null.");

        // Find current record if exists
        final Property currentProperty = find(newProperty.getDmpIdentifier(), newProperty.getClassType(),
                newProperty.getClassIdentifier(), newProperty.getPropertyName(), null, newProperty.getReference());

        if (currentProperty != null) {
            // If values are different
            if (!currentProperty.hasSameValue(newProperty)) {
                // Set new value
                currentProperty.setValue(newProperty.getValue());
                // Update property
                update(currentProperty, rdmService);
            }
        } else {
            // Persist new property
            persist(newProperty, rdmService);
        }
    }

    /**
     * 
     * Update or create new properties
     * 
     * @param properties
     * @param rdmService
     */
    @Transactional
    public void updateOrCreateProperties(List<Property> properties, RDMService rdmService) {
        Objects.requireNonNull(properties, "List with properties is null.");

        // For each property
        for (Property property : properties) {
            updateOrCreateProperty(property, rdmService);
        }
    }

    /**
     * 
     * Delete the property
     * 
     * @param property
     * @param rdmService
     */
    @Transactional
    public void deleteProperty(Property property) {
        Objects.requireNonNull(property, "Property is null.");

        // Find the property
        final Property oldProperty = find(property.getDmpIdentifier(), property.getClassType(),
                property.getClassIdentifier(), property.getPropertyName(), null, property.getReference());

        if (oldProperty != null) {
            propertyDao.delete(oldProperty);
        }
    }

    /**
     * 
     * Delete the properties
     * 
     * @param properties
     * @param rdmService
     */
    @Transactional
    public void deleteProperties(List<Property> properties) {
        Objects.requireNonNull(properties, "Properties is null.");

        // For each property
        for (Property property : properties) {
            deleteProperty(property);
        }
    }

    /**
     * 
     * Load all identifiers of class
     * 
     * @param dmpIdentifier
     * @param classType
     * @param propertyName
     * @return
     */
    public List<Property> loadAllIdentifiers(String dmpIdentifier, String classType, String propertyName) {
        return propertyDao.findIdentifiers(dmpIdentifier, classType, propertyName);
    }

    /**
     * 
     * Remove all nested properties
     * Note: the function identified instances in specific DMP only by references,
     * not by maDMP class types
     * 
     * @param DMPIdentifier
     * @param identifier
     */
    @Transactional
    public void removeAllNestedProperties(String DMPIdentifier, String identifier) {
        Objects.requireNonNull(DMPIdentifier, "DMP identifier is null.");
        Objects.requireNonNull(identifier, "Identifier is null.");

        // End all nested properties
        List<Property> properties = findProperties(DMPIdentifier, null, null, null, null, identifier);
        for (final Property property : properties) {
            if (!identifier.equals(property.getClassIdentifier())) {
                removeAllNestedProperties(DMPIdentifier, property.getClassIdentifier());
            }
        }

        // End all class properties
        deleteProperties(findProperties(DMPIdentifier, null, identifier, null, null, null));
    }

    /**
     * 
     * Change class identifiers and references of nested instances
     * Note: the function identified instances in specific DMP only by identifiers,
     * not by maDMP class types
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
