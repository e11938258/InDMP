package at.tuwien.indmp.model.dmp;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.service.PropertyService;
import at.tuwien.indmp.util.Functions;

public class SecurityAndPrivacy extends ClassEntity {

    /* Properties */
    private String description;

    @NotNull
    private String title;

    public SecurityAndPrivacy() {
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return getTitle();
    }

    @Override
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String reference, RDMService rdmService) {
        final List<Property> properties = new ArrayList<>();

        final Property property = new Property(dmp.getClassIdentifier(), getClassType(), getClassIdentifier(),
                "title", getClassIdentifier(), reference);
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
        p = Functions.findPropertyInList("title", properties);
        setTitle(p.getValue());
    }
}
