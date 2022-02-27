package at.tuwien.simulation.model.dmp;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import at.tuwien.simulation.model.Entity;

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

    public Contributor(String mbox, String name, List<String> role, Contributor_id contributor_id) {
        this.mbox = mbox;
        this.name = name;
        this.role = role;
        this.contributor_id = contributor_id;
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
                getRole() != null ? getRole().toString() : null,
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
    public List<Entity> getPropertiesFromIdentifier(DMP dmp, String location) {
        return getContributor_id().getProperties(dmp, getLocation(location));
    }
}
