package at.tuwien.indmp.model.dmp;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import at.tuwien.indmp.exception.ForbiddenException;
import at.tuwien.indmp.model.DataService;
import at.tuwien.indmp.model.Entity;
import at.tuwien.indmp.util.Functions;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractEntity {

    @JsonIgnore
    public abstract Object[] getValues();

    @JsonIgnore
    public abstract String[] getValueNames();

    @JsonIgnore
    public abstract String getClassIdentifier();

    @JsonIgnore
    public String getClassType() {
        return getClass().getSimpleName().toLowerCase();
    }

    @JsonIgnore
    public boolean areSame(String identifier) {
        return getClassIdentifier().equals(identifier);
    }

    @JsonIgnore
    public String getLocation(String location) {
        if (getClassIdentifier().equals(null)) {
            throw new ForbiddenException("Null identifier!");
        } else {
            return location + "/" + getClassIdentifier();
        }
    }

    @JsonIgnore
    public List<Entity> getProperties(DMP dmp, String location, DataService dataService) {
        final List<Entity> properties = new ArrayList<>();

        // Get current values and their names
        final Object[] values = getValues();
        final String[] propertyNames = getValueNames();

        // Same length of arrays?
        if (values.length != propertyNames.length) {
            throw new ForbiddenException("Lengths are not same!");
        }

        // For each value
        for (int i = 0; i < values.length; i++) {
            // If value is not null
            if (values[i] != null) {
                properties.add(Functions.createEntity(dmp, getLocation(location),
                        getClassType() + ":" + propertyNames[i], values[i].toString()));
            }
        }

        return properties;
    }

}
