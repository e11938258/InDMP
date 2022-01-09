package at.tuwien.indmp.model.dmp;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.util.dmp.GrantIdentifierType;

public class Metadata_standard_id extends Identifier {

    /* Properties */
    @NotNull
    private GrantIdentifierType type;

    public Metadata_standard_id() {
        super(null);
    }

    public Metadata_standard_id(String identifier, GrantIdentifierType type) {
        super(identifier);
        this.type = type;
    }

    public GrantIdentifierType getType() {
        return this.type;
    }

    public void setType(GrantIdentifierType type) {
        this.type = type;
    }
}
