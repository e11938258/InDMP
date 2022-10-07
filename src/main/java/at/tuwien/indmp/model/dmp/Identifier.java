package at.tuwien.indmp.model.dmp;

import javax.validation.constraints.NotNull;

public abstract class Identifier extends AbstractObject {

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

    @Override
    public String getObjectIdentifier() {
        return this.identifier;
    }

    @Override
    public abstract String getObjectType();

    @Override
    public Object[] getValues() {
        return new Object[] {
                getObjectIdentifier(),
                getType(),
        };
    }

    @Override
    public String[] getPropertyNames() {
        return new String[] {
                "identifier",
                "type",
        };
    }

    @Override
    public String toString() {
        return "{" +
                " identifier='" + getObjectIdentifier() + "'" +
                ", type='" + getType() + "'" +
                "}";
    }

}
