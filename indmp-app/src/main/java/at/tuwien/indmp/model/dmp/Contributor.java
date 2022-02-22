package at.tuwien.indmp.model.dmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.model.DataService;
import at.tuwien.indmp.model.Entity;
import at.tuwien.indmp.service.EntityService;
import at.tuwien.indmp.util.Functions;

public class Contributor extends AbstractClassEntity {

    /* Properties */
    private String mbox;

    @NotNull
    private String name;

    private List<String> role = new ArrayList<>();

    /* Nested data structure */
    @NotNull
    private Contributor_id contributor_id;

    public Contributor() {
    }

    public String getMbox() {
        return this.mbox;
    }

    public void setMbox(String mbox) {
        this.mbox = mbox;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRole() {
        return this.role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public Contributor_id getContributor_id() {
        return this.contributor_id;
    }

    public void setContributor_id(Contributor_id contributor_id) {
        this.contributor_id = contributor_id;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getMbox(),
                getName(),
                getRole().toString(),
        };
    }

    @Override
    public String[] getValueNames() {
        return new String[] {
                "mbox",
                "name",
                "role",
        };
    }

    @Override
    public String getClassIdentifier() {
        return getContributor_id().getClassIdentifier();
    }

    @Override
    public List<Entity> getPropertiesFromIdentifier(DMP dmp, String location, DataService dataService) {
        return getContributor_id().getProperties(dmp, getLocation(location), dataService);
    }

    @Override
    public void build(EntityService entityService, String location) {
        // Set properties
        final List<Entity> properties = entityService.findEntities(location, null);

        Entity p = Functions.findPropertyInList(getClassType(), "mbox", properties);
        setMbox(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getClassType(), "name", properties);
        setName(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getClassType(), "role", properties);
        setRole(p != null
                ? Arrays.asList(p.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",", -1))
                : null);

        // Set identifier
        final Entity identifier = Functions.findPropertyInList(getClassType(), "identifier", properties);
        final Entity type = Functions.findPropertyInList(getClassType(), "type", properties);
        setContributor_id(new Contributor_id(identifier.getValue(), type.getValue()));
    }
}
