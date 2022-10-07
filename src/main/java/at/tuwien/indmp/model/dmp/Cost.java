package at.tuwien.indmp.model.dmp;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.modul.PropertyModule;
import at.tuwien.indmp.util.ModelConstants;
import at.tuwien.indmp.util.Functions;

public class Cost extends AbstractClassObject {

    /* Properties */
    @Pattern(regexp = ModelConstants.REGEX_ISO_4212)
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
                getTitle()
        };
    }

    @Override
    public String[] getPropertyNames() {
        return new String[] {
                "currency_code",
                "description",
                "value",
                "title"
        };
    }

    @Override
    public String getObjectIdentifier() {
        return getTitle();
    }

    @Override
    public void build(PropertyModule propertyModule, String atLocation) {
        // ------------------------------------
        // Set properties
        // ------------------------------------
        final List<Property> properties = propertyModule.findEntities(atLocation, null, null, true);

        Property p = Functions.findPropertyInList(getObjectType(), "currency_code", properties);
        setCurrency_code(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getObjectType(), "description", properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getObjectType(), "value", properties);
        setValue(p != null ? Double.parseDouble(p.getValue()) : null);

        // ------------------------------------
        // Set identifier
        // ------------------------------------
        p = Functions.findPropertyInList(getObjectType(), "title", properties);
        setTitle(p != null ? p.getValue() : null);
    }
}
