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
     * Create a new property
     * 
     * @param dmp
     * @param atLocation
     * @param specializationOf
     * @param value
     * @return
     */
    public static Property createProperty(DMP dmp, String atLocation, String specializationOf, String value) {
        // Create a new activity
        final Activity activity = new Activity(Timestamp.valueOf(dmp.getModified()));

        // Add a new property
        final Property property = new Property(atLocation, specializationOf, value, activity);

        // Set property to activity
        activity.setGenerated(property);
        
        // Return the property
        return property;
    }

    /**
     * 
     * Find property in the list of properties
     * 
     * @param specializationOf
     * @param properties
     * @return
     */
    public static Property findPropertyInList(String specializationOf, List<Property> properties) {
        final List<Property> results = properties.stream()
                .filter(p -> p.getSpecializationOf().equals(specializationOf))
                .collect(Collectors.toList());
        
        // If the property was found
        if (results.size() == 1) {
            return results.get(0);
        } else {
            return null;
        }
    }
}
