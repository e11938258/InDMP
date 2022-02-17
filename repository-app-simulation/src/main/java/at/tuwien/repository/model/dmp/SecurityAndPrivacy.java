package at.tuwien.repository.model.dmp;

import javax.validation.constraints.NotNull;

public class SecurityAndPrivacy {

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
}
