package at.tuwien.indmp.model.dmp;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.indmp.model.Entity;
import at.tuwien.indmp.service.EntityService;
import at.tuwien.indmp.util.ModelConstants;
import at.tuwien.indmp.util.Functions;

public class Cost extends AbstractClassEntity {

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
    public String[] getValueNames() {
        return new String[] {
                "currency_code",
                "description",
                "value",
                "title"
        };
    }

    @Override
    public String getClassIdentifier() {
        return getTitle();
    }

    @Override
    public void build(EntityService entityService, String location) {
        // Set properties
        final List<Entity> properties = entityService.findEntities(location, null, null, true);

        Entity p = Functions.findPropertyInList(getClassType(), "currency_code", properties);
        setCurrency_code(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getClassType(), "description", properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getClassType(), "value", properties);
        setValue(p != null ? Double.parseDouble(p.getValue()) : null);

        // Set identifier
        p = Functions.findPropertyInList(getClassType(), "title", properties);
        setTitle(p != null ? p.getValue() : null);
    }
}
