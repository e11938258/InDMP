package at.tuwien.simulation.model.dmp;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import at.tuwien.simulation.model.Entity;
import at.tuwien.simulation.util.Functions;

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
    public String getLocation(String location) {
        if (getClassIdentifier().equals(null)) {
            throw new ValidationException("Null identifier!");
        } else {
            return location + "/" + getClassIdentifier();
        }
    }

    @JsonIgnore
    public List<Entity> getProperties(DMP dmp, String location) {
        final List<Entity> properties = new ArrayList<>();

        // Get current values and their names
        final Object[] values = getValues();
        final String[] propertyNames = getValueNames();

        // Same length of arrays?
        if (values.length != propertyNames.length) {
            throw new ValidationException("Lengths are not same!");
        }

        // For each value
        for (int i = 0; i < values.length; i++) {
            // If value is not null
            if (values[i] != null) {
                properties.add(Functions.createEntity(getLocation(location), getClassType() + ":" + propertyNames[i],
                        values[i].toString()));
            }
        }

        return properties;
    }

}
