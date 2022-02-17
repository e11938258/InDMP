package at.tuwien.repository.model.dmp;

import javax.validation.constraints.NotNull;

public class TechnicalResource {

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
}
