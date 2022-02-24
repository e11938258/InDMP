package at.tuwien.simulation.model.dmp;

import javax.validation.constraints.NotNull;

public class SecurityAndPrivacy extends AbstractClassEntity {

    /* Properties */
    private String description;

    @NotNull
    private String title;

    public SecurityAndPrivacy() {
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getDescription(),
                getTitle()
        };
    }

    @Override
    public String[] getValueNames() {
        return new String[] {
                "description",
                "title"
        };
    }

    @Override
    public String getClassIdentifier() {
        return getTitle();
    }
}
