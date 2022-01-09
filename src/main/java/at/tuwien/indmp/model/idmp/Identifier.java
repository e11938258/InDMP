package at.tuwien.indmp.model.idmp;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.util.idmp.ClassType;

public class Identifier {

    /* Properties */
    @NotNull
    private ClassType type;

    /* Nested data structure */
    private ElementIdentifier id;

    private ElementIdentifier new_id;

    public Identifier() {
    }

    public Identifier(ClassType type, ElementIdentifier id, ElementIdentifier new_id) {
        this.type = type;
        this.id = id;
        this.new_id = new_id;
    }

    public ClassType getType() {
        return this.type;
    }

    public void setType(ClassType type) {
        this.type = type;
    }

    public ElementIdentifier getId() {
        return this.id;
    }

    public void setId(ElementIdentifier id) {
        this.id = id;
    }

    public ElementIdentifier getNew_id() {
        return this.new_id;
    }

    public void setNew_id(ElementIdentifier new_id) {
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
