package at.tuwien.indmp.model.dmp;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.module.PropertyModule;
import at.tuwien.indmp.model.Property;

public abstract class AbstractClassObject extends AbstractObject {

    /**
     * 
     * Build the object
     * 
     * @param propertyModule
     * @param atLocation
     */
    public abstract void build(PropertyModule propertyModule, String atLocation);

    /**
     * 
     * Get properties from the property nested identifier object (identfier + type)
     * 
     * @param dmp
     * @param atLocation
     * @param rdmService
     * @return
     */
    @JsonIgnore
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String atLocation, RDMService rdmService) {
        return new ArrayList<Property>();
    }

    /**
     * 
     * Get the properties from the nested objects
     * 
     * @param dmp
     * @param atLocation
     * @param rdmService
     * @return
     */
    @JsonIgnore
    public List<Property> getPropertiesFromNestedObjects(DMP dmp, String atLocation, RDMService rdmService) {
        return new ArrayList<Property>();
    }

    @JsonIgnore
    @Override
    public List<Property> getProperties(DMP dmp, String atLocation, RDMService rdmService) {
        final List<Property> properties = super.getProperties(dmp, atLocation, rdmService);

        // Add the object identifier
        properties.addAll(getPropertiesFromIdentifier(dmp, atLocation, rdmService));

        return properties;
    }
}
