package at.tuwien.indmp.model.dmp;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.System;
import at.tuwien.indmp.service.PropertyService;

public abstract class ClassEntity extends Entity {

    @JsonIgnore
    public abstract List<Property> getPropertiesFromIdentifier(DMP dmp, String reference, System system);

    @JsonIgnore
    public abstract List<Property> getPropertiesFromNestedClasses(DMP dmp, System system);

    @JsonIgnore
    public abstract boolean hasRightsToUpdate(System system);

    @JsonIgnore
    @Override
    public List<Property> getProperties(DMP dmp, String reference, System system) {
        if (hasRightsToUpdate(system)) {
            final List<Property> properties = super.getProperties(dmp, reference, system);

            // Add identifier
            properties.addAll(getPropertiesFromIdentifier(dmp, reference, system));

            return properties;
        } else {
            return new ArrayList<>();
        }
    }

    public abstract void build(PropertyService propertyService, String dmpIdentifier, String classIdentifier);
}
