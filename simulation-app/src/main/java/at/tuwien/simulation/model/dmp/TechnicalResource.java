package at.tuwien.simulation.model.dmp;

import javax.validation.constraints.NotNull;

public class TechnicalResource extends AbstractClassEntity {

    /* Properties */
    private String description;

    @NotNull
    private String name;

    public TechnicalResource() {
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getDescription(),
                getName()
        };
    }

    @Override
    public String[] getValueNames() {
        return new String[] {
                "description",
                "name"
        };
    }

    @Override
    public String getClassIdentifier() {
        return getName();
    }
}
