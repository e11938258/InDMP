package at.tuwien.repository.model.dmp;

import javax.validation.constraints.NotNull;

public abstract class Identifier {

    @NotNull
    private String identifier;

    public abstract Object getType();

    public Identifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}
