package at.tuwien.indmp.model.dmp;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import at.tuwien.indmp.exception.ForbiddenException;
import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.util.Functions;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractObject {

    /**
     * 
     * Get values of the object properties
     * 
     * @return
     */
    @JsonIgnore
    public abstract Object[] getValues();

    /**
     * 
     * Get the object property names
     * 
     * @return
     */
    @JsonIgnore
    public abstract String[] getPropertyNames();

    /**
     * 
     * Get the object identifier
     * 
     * @return
     */
    @JsonIgnore
    public abstract String getObjectIdentifier();

    /**
     * 
     * Get the object type
     * 
     * @return
     */
    @JsonIgnore
    public String getObjectType() {
        return getClass().getSimpleName().toLowerCase();
    }

    /**
     * 
     * Are the object same by the identifier?
     * 
     * @param identifier
     * @return
     */
    @JsonIgnore
    public boolean areSame(String identifier) {
        return getObjectIdentifier().equals(identifier);
    }

    /**
     * 
     * Generate the object atLocation
     * 
     * @param atLocation
     * @return
     */
    @JsonIgnore
    public String getAtLocation(String atLocation) {
        if (getObjectIdentifier().equals(null)) {
            throw new ForbiddenException("Null identifier!");
        } else {
            return atLocation + "/" + getObjectIdentifier();
        }
    }

    /**
     * 
     * Generate the property specializationOf
     * 
     * @param propertyName
     * @return
     */
    @JsonIgnore
    public String getSpecializationOf(String propertyName) {
        return getObjectType() + ":" + propertyName;
    }

    /**
     * 
     * Get the properties of the object
     * 
     * @param dmp
     * @param atLocation
     * @param rdmService
     * @return
     */
    @JsonIgnore
    public List<Property> getProperties(DMP dmp, String atLocation, RDMService rdmService) {
        final List<Property> properties = new ArrayList<>();

        // Get current values and property names
        final Object[] values = getValues();
        final String[] propertyNames = getPropertyNames();

        // Same length of arrays?
        if (values.length != propertyNames.length) {
            throw new ForbiddenException("Lengths are not same!");
        }

        // 6.1 For each property from received maDMP
        for (int i = 0; i < values.length; i++) {
            // If value is not null
            if (values[i] != null && !values[i].equals("[]")) {
                // Create a new property instance
                final Property property = Functions.createProperty(dmp, getAtLocation(atLocation),
                        getSpecializationOf(propertyNames[i]), values[i].toString());
                // 6.1.1 6.1.2 If RDM service has the right to change the property
                // 7. 7.1 All properties from maDMP are stored.
                if (dmp.isNew() || rdmService.hasPropertyRight(property)) {
                    properties.add(property);
                }
            }
        }

        return properties;
    }

}
