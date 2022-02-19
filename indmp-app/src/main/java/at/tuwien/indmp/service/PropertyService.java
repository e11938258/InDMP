package at.tuwien.indmp.service;

import java.util.List;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.RDMService;

public interface PropertyService {

    public void persist(Property property, RDMService rdmService);

    public void persist(List<Property> properties, RDMService rdmService);

    public Property find(String dmpIdentifier, String classType, String classIdentifier, String propertyName,
            String value, String reference);

    public Property find(Property property);

    public List<Property> findProperties(String dmpIdentifier, String classType, String classIdentifier,
            String propertyName, String value, String reference);

    public List<Property> findProperties(Property property);

    public void update(Property property, RDMService rdmService);

    public void loadAndUpdate(String dmpIdentifier, String classType, String classIdentifier, String propertyName,
            String newValue, String reference, RDMService rdmService);

    public void updateOrCreateProperty(Property newProperty, RDMService rdmService);

    public void updateOrCreateProperties(List<Property> properties, RDMService rdmService);

    public void deleteProperty(Property property);

    public void deleteProperties(List<Property> properties);

    public List<Property> loadAllIdentifiers(String dmpIdentifier, String classType, String propertyName);

    public void removeAllNestedProperties(String DMPIdentifier, String identifier);

    public void changeNestedIdentifiers(String DMPIdentifier, String identifier, String newIdentifier);
}
