package at.tuwien.indmp.model.dmp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.indmp.util.DMPConstants;

public class Grant_id extends Identifier {

    /* Properties */
    @NotNull
    @Pattern(regexp = DMPConstants.REGEX_GRANT_IDENTIFIER_TYPE)
    private String type;

    public Grant_id() {
        super(null);
    }

    public Grant_id(String identifier, String type) {
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
