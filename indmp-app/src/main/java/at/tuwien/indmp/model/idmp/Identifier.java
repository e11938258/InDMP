package at.tuwien.indmp.model.idmp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.indmp.util.DMPConstants;

public class Identifier {

    /* Properties */
    @NotNull
    private String identifier;

    @Pattern(regexp = DMPConstants.REGEX_ALL_IDENTIFIER_TYPES)
    private String type;

    public Identifier() {
    }

    public Identifier(String identifier, String type) {
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
