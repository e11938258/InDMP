package at.tuwien.indmp.model.dmp;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.util.dmp.DataIdentifierType;

public class Dmp_id extends Identifier {

    /* Properties */
    @NotNull
    private DataIdentifierType type;

    public Dmp_id() {
        super(null);
    }

    public Dmp_id(String identifier, DataIdentifierType type) {
        super(identifier);
        this.type = type;
    }

    public DataIdentifierType getType() {
        return this.type;
    }

    public void setType(DataIdentifierType type) {
        this.type = type;
    }
}
