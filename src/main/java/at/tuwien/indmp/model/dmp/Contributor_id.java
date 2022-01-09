package at.tuwien.indmp.model.dmp;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.util.dmp.PersonIdentifierType;

public class Contributor_id extends Identifier {

    /* Properties */
    @NotNull
    private PersonIdentifierType type;

    public Contributor_id() {
        super(null);
    }

    public Contributor_id(String identifier, PersonIdentifierType type) {
        super(identifier);
        this.type = type;
    }
    public PersonIdentifierType getType() {
        return this.type;
    }

    public void setType(PersonIdentifierType type) {
        this.type = type;
    }
}
