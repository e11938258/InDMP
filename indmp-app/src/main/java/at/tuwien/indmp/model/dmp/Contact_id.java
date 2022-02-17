package at.tuwien.indmp.model.dmp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.indmp.util.DMPConstants;

public class Contact_id extends Identifier {

    /* Properties */
    @NotNull
    @Pattern(regexp = DMPConstants.REGEX_CONTACT_IDENTIFIER_TYPE)
    private String type;

    public Contact_id() {
        super(null);
    }

    public Contact_id(String identifier, String type) {
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
