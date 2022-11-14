package at.tuwien.indmp.model.dmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.module.PropertyModule;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.util.Functions;

public class Contributor extends AbstractClassObject {

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
                getRole() != null ? getRole().toString() : null,
        };
    }

    @Override
    public String[] getPropertyNames() {
        return new String[] {
                "mbox",
                "name",
                "role",
        };
    }

    @Override
    public String getObjectIdentifier() {
        return getContributor_id().getObjectIdentifier();
    }

    @Override
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String atLocation, RDMService rdmService) {
        return getContributor_id().getProperties(dmp, atLocation, rdmService);
    }

    @Override
    public void build(PropertyModule propertyModule, String atLocation) {
        // ------------------------------------
        // Set properties
        // ------------------------------------
        final List<Property> properties = propertyModule.findProperties(atLocation, null, null, true);

        Property p = Functions.findPropertyInList(getSpecializationOf("mbox"), properties);
        setMbox(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getSpecializationOf("name"), properties);
        setName(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getSpecializationOf("role"), properties);
        setRole(p != null
                ? Arrays.asList(p.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",", -1))
                : null);

        // ------------------------------------
        // Set identifier
        // ------------------------------------
        final Property identifier = Functions.findPropertyInList(getSpecializationOf("identifier"), properties);
        final Property type = Functions.findPropertyInList(getSpecializationOf("type"), properties);
        setContributor_id(new Contributor_id(identifier.getValue(), type.getValue()));
    }
}
