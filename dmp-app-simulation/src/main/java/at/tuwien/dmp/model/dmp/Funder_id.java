package at.tuwien.dmp.model.dmp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.dmp.util.DMPConstants;

public class Funder_id extends Identifier {

    /* Properties */
    @NotNull
    @Pattern(regexp = DMPConstants.REGEX_FUNDER_IDENTIFIER_TYPE)
    private String type;

    public Funder_id() {
        super(null);
    }

    public Funder_id(String identifier, String type) {
        super(identifier);
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
