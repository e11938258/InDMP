package at.tuwien.indmp.model.dmp;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.service.PropertyService;
import at.tuwien.indmp.util.Functions;

public class TechnicalResource extends ClassEntity {

    /* Properties */
    private String description;

    @NotNull
    private String name;

    public TechnicalResource() {
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getDescription(),
        };
    }

    @Override
    public String[] getValueNames() {
        return new String[] {
                "description",
        };
    }

    @Override
    public String getClassIdentifier() {
        return getName();
    }

    @Override
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String reference, RDMService rdmService) {
        final List<Property> properties = new ArrayList<>();

        final Property property = new Property(dmp.getClassIdentifier(), getClassType(), getClassIdentifier(),
                "name", getClassIdentifier(), reference);
        properties.add(property);

        return properties;
    }

    @Override
    public void build(PropertyService propertyService, String dmpIdentifier, String classIdentifier) {
        // Set properties
        final List<Property> properties = propertyService.findProperties(dmpIdentifier, getClassType(),
                classIdentifier, null, null, null);

        Property p = Functions.findPropertyInList("description", properties);
        setDescription(p != null ? p.getValue() : null);

        // Set identifier
        p = Functions.findPropertyInList("name", properties);
        setName(p.getValue());
    }
}
