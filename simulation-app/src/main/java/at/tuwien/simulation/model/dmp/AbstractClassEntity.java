package at.tuwien.simulation.model.dmp;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.tuwien.simulation.model.Entity;

public abstract class AbstractClassEntity extends AbstractEntity {

    @JsonIgnore
    public List<Entity> getPropertiesFromIdentifier(DMP dmp, String location) {
        return new ArrayList<Entity>();
    }

    @JsonIgnore
    public List<Entity> getPropertiesFromNestedClasses(DMP dmp, String location) {
        return new ArrayList<Entity>();
    }

    @JsonIgnore
    @Override
    public List<Entity> getProperties(DMP dmp, String location) {
        final List<Entity> properties = super.getProperties(dmp, location);

        // Add identifier
        properties.addAll(getPropertiesFromIdentifier(dmp, location));

        return properties;
    }
}
