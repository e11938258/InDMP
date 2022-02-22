package at.tuwien.indmp.model.dmp;

import java.util.List;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.model.Entity;
import at.tuwien.indmp.service.EntityService;
import at.tuwien.indmp.util.Functions;

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

    @Override
    public void build(EntityService entityService, String location) {
        // Set properties
        final List<Entity> properties = entityService.findEntities(location, null);

        Entity p = Functions.findPropertyInList(getClassType(), "description", properties);
        setDescription(p != null ? p.getValue() : null);

        // Set identifier
        p = Functions.findPropertyInList(getClassType(),"name", properties);
        setName(p.getValue());
    }
}
