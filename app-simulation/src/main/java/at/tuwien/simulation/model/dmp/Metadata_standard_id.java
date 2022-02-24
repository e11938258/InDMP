package at.tuwien.simulation.model.dmp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.simulation.util.ModelConstants;

public class Metadata_standard_id extends Identifier {

    /* Properties */
    @NotNull
    @Pattern(regexp = ModelConstants.REGEX_METADATA_IDENTIFIER_TYPE)
    private String type;

    public Metadata_standard_id() {
        super(null);
    }

    public Metadata_standard_id(String identifier, String type) {
        super(identifier);
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getClassType() {
        return "metadata";
    }
}
