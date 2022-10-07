package at.tuwien.indmp.util;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import at.tuwien.indmp.model.Activity;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.dmp.DMP;

public class Functions {

    private Functions() {
        throw new AssertionError();
    }

    /**
     * 
     * Find property in the list of entities
     * 
     * @param classType
     * @param name
     * @param entities
     * @return
     */
    public static Property findPropertyInList(String classType, String name, List<Property> entities) {
        final String propertyName = classType + ":" + name;
        final List<Property> results = entities.stream().filter(p -> p.getSpecializationOf().equals(propertyName))
                .collect(Collectors.toList());
        if(results.size() == 1) {
            return results.get(0);
        } else {
            return null;
        }
    }

    /**
     * 
     * Create a new property
     * 
     * @param dmp
     * @param location
     * @param propertyName
     * @param value
     * @return
     */
    public static Property createProperty(DMP dmp, String location, String propertyName, String value) {
        // Create a new activity
        final Activity activity = new Activity(Timestamp.valueOf(dmp.getModified()));
        // Add a new property
        final Property property = new Property(location, propertyName, value, activity);
        // Set property to activity
        activity.setGenerated(property);
        // Return the property
        return property;
    }
}
