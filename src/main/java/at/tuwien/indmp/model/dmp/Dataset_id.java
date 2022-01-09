package at.tuwien.indmp.model.dmp;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.util.dmp.DataIdentifierType;

public class Dataset_id extends Identifier {

    /* Properties */
    @NotNull
    private DataIdentifierType type;

    public Dataset_id() {
        super(null);
    }

    public Dataset_id(String identifier, DataIdentifierType type) {
        super(identifier);
        this.type = type;
    }

    public Dataset_id(String identifier, String type) {
        super(identifier);
        this.type = DataIdentifierType.valueOf(type);
    }

    public DataIdentifierType getType() {
        return this.type;
    }

    public void setType(DataIdentifierType type) {
        this.type = type;
    }
}
