package at.tuwien.simulation.model.dmp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.simulation.util.ModelConstants;

public class Dataset_id extends Identifier {

    /* Properties */
    @NotNull
    @Pattern(regexp = ModelConstants.REGEX_DATA_IDENTIFIER_TYPE)
    private String type;

    public Dataset_id() {
        super(null);
    }

    public Dataset_id(String identifier, String type) {
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
        return "dataset";
    }
}
