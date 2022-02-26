package at.tuwien.simulation.model.dmp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.simulation.util.ModelConstants;

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

    public Cost(String currency_code, String description, String title, Double value) {
        this.currency_code = currency_code;
        this.description = description;
        this.title = title;
        this.value = value;
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
}
