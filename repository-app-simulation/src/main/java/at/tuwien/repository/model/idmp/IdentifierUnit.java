package at.tuwien.repository.model.idmp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.repository.util.DMPConstants;

public class IdentifierUnit {

    /* Properties */
    @NotNull
    @Pattern(regexp = DMPConstants.REGEX_DMP_CLASS_TYPE_WITHOUT_ID)
    private String type;

    /* Nested data structure */
    @NotNull
    private Identifier id;

    private Identifier new_id;

    public IdentifierUnit() {
    }

    public IdentifierUnit(String type, Identifier id, Identifier new_id) {
        this.type = type;
        this.id = id;
        this.new_id = new_id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Identifier getId() {
        return this.id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public Identifier getNew_id() {
        return this.new_id;
    }

    public void setNew_id(Identifier new_id) {
        this.new_id = new_id;
    }

    @Override
    public String toString() {
        return "{" +
                " type='" + getType() + "'" +
                ", id='" + getId() + "'" +
                "}";
    }

}
