package at.tuwien.indmp.model.dmp;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.util.dmp.FunderIdentifierType;

public class Funder_id extends Identifier {

    /* Properties */
    @NotNull
    private FunderIdentifierType type;

    public Funder_id() {
        super(null);
    }

    public Funder_id(String identifier, FunderIdentifierType type) {
        super(identifier);
        this.type = type;
    }

    public FunderIdentifierType getType() {
        return this.type;
    }

    public void setType(FunderIdentifierType type) {
        this.type = type;
    }
}
