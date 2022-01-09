package at.tuwien.indmp.model.idmp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import at.tuwien.indmp.util.Constants;

public class ElementIdentifier {

    /* Properties */
    @NotNull
    private String identifier;

    @Size(min = Constants.TYPES_MIN, max = Constants.TYPES_MAX)
    @Pattern(regexp = Constants.TYPES_REGEX)
    private String type;

    public ElementIdentifier() {
    }

    public ElementIdentifier(String identifier, String type) {
        this.identifier = identifier;
        this.type = type;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
