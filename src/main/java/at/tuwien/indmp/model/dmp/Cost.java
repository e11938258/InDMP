package at.tuwien.indmp.model.dmp;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.service.PropertyService;
import at.tuwien.indmp.util.DMPConstants;
import at.tuwien.indmp.util.Functions;

public class Cost extends ClassEntity {

    /* Properties */
    @Pattern(regexp = DMPConstants.REGEX_ISO_4212)
    private String currency_code;

    private String description;

    @NotNull
    private String title;

    private Double value;

    public Cost() {
    }

    public String getCurrency_code() {
        return this.currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
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

    public Double getValue() {
        return this.value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getCurrency_code(),
                getDescription(),
                getValue(),
        };
    }

    @Override
    public String[] getValueNames() {
        return new String[] {
                "currency_code",
                "description",
                "value",
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
                "title", getTitle(), reference);
        properties.add(property);

        return properties;
    }

    @Override
    public void build(PropertyService propertyService, String dmpIdentifier, String classIdentifier) {
        // Set properties
        final List<Property> properties = propertyService.findProperties(dmpIdentifier, getClassType(), classIdentifier,
                null,
                null, null);

        Property p = Functions.findPropertyInList("currency_code", properties);
        setCurrency_code(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("description", properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("value", properties);
        setValue(p != null ? Double.parseDouble(p.getValue()) : null);

    }
}
