package at.tuwien.indmp.model.dmp;

import javax.validation.constraints.Pattern;

import at.tuwien.indmp.exception.ForbiddenException;
import at.tuwien.indmp.util.ModelConstants;

public class DMP_id extends Identifier {

    /* Properties */
    @Pattern(regexp = ModelConstants.REGEX_DATA_IDENTIFIER_TYPE)
    private String type;

    public DMP_id() {
        super(null);
    }

    public DMP_id(String identifier) {
        super(identifier);
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getLocation(String location) {
        if (getClassIdentifier().equals(null)) {
            throw new ForbiddenException("Null identifier!");
        } else {
            return location + "/" + getClassIdentifier();
        }
    }

    @Override
    public String getClassType() {
        return "dmp";
    }
}
