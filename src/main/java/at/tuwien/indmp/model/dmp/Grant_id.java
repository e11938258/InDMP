package at.tuwien.indmp.model.dmp;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.util.dmp.GrantIdentifierType;

public class Grant_id extends Identifier {

    /* Properties */
    @NotNull
    private GrantIdentifierType type;


    public Grant_id() {
        super(null);
    }

    public Grant_id(String identifier, GrantIdentifierType type) {
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
