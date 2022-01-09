package at.tuwien.indmp.model.dmp;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import at.tuwien.indmp.exception.ForbiddenException;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.System;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Entity {

    @JsonIgnore
    public String getClassName() {
        return getClass().getSimpleName().toLowerCase();
    }

    @JsonIgnore
    public abstract Object[] getValues();

    @JsonIgnore
    public abstract String[] getValueNames();

    @JsonIgnore
    public abstract String getClassIdentifier();

    @JsonIgnore
    public boolean areSame(String identifier) {
        return getClassIdentifier().equals(identifier);
    }

    @JsonIgnore
    public List<Property> getProperties(DMP dmp, String reference, System system) {
        final List<Property> properties = new ArrayList<>();
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
                // Add a new property
                final Property property = new Property(dmp.getClassIdentifier(), getClassName(), getClassIdentifier(),
                propertyNames[i], values[i].toString(), reference, dmp.getModified(), system);
                properties.add(property);
                system.add(property);
            }
        }

        return properties;
    }

}
