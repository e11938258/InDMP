package at.tuwien.indmp.util;

import java.util.List;
import java.util.stream.Collectors;

import at.tuwien.indmp.model.Activity;
import at.tuwien.indmp.model.Entity;
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
    public static Entity findPropertyInList(String classType, String name, List<Entity> entities) {
        final String propertyName = classType + ":" + name;
        final List<Entity> results = entities.stream().filter(p -> p.getSpecializationOf().equals(propertyName))
                .collect(Collectors.toList());
        if(results.size() == 1) {
            return results.get(0);
        } else {
            return null;
        }
    }

    /**
     * 
     * Create a new entity
     * 
     * @param dmp
     * @param location
     * @param propertyName
     * @param value
     * @return
     */
    public static Entity createEntity(DMP dmp, String location, String propertyName, String value) {
        // Create a new activity
        final Activity activity = new Activity(dmp.getModified());
        // Add a new entity
        final Entity entity = new Entity(location, propertyName, value, activity);
        // Set entity to activity
        activity.setGenerated(entity);
        return entity;
    }
}
