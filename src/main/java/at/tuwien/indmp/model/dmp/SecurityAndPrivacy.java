package at.tuwien.indmp.model.dmp;

import java.util.List;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.model.Entity;
import at.tuwien.indmp.service.EntityService;
import at.tuwien.indmp.util.Functions;

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

    @Override
    public void build(EntityService entityService, String location) {
        // Set properties
        final List<Entity> properties = entityService.findEntities(location, null, null, true);

        Entity p = Functions.findPropertyInList(getClassType(),"description", properties);
        setDescription(p != null ? p.getValue() : null);

        // Set identifier
        p = Functions.findPropertyInList(getClassType(),"title", properties);
        setTitle(p.getValue());
    }
}
