package at.tuwien.simulation.model.dmp;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.simulation.model.Entity;
import at.tuwien.simulation.util.ModelConstants;

public class Funding extends AbstractClassEntity {

    /* Properties */
    @Pattern(regexp = ModelConstants.REGEX_FUNDING_STATUS)
    private String funding_status;

    /* Nested data structure */
    @NotNull
    private Funder_id funder_id;

    private Grant_id grant_id;

    public Funding() {
    }

    public String getFunding_status() {
        return this.funding_status;
    }

    public void setFunding_status(String funding_status) {
        this.funding_status = funding_status;
    }

    public Funder_id getFunder_id() {
        return this.funder_id;
    }

    public void setFunder_id(Funder_id funder_id) {
        this.funder_id = funder_id;
    }

    public Grant_id getGrant_id() {
        return this.grant_id;
    }

    public void setGrant_id(Grant_id grant_id) {
        this.grant_id = grant_id;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getFunding_status(),
        };
    }

    @Override
    public String[] getValueNames() {
        return new String[] {
                "funding_status",
        };
    }

    @Override
    public String getClassIdentifier() {
        return getFunder_id().getClassIdentifier();
    }

    @Override
    public List<Entity> getPropertiesFromIdentifier(DMP dmp, String location) {
        return getFunder_id().getProperties(dmp, location);
    }

    @Override
    public List<Entity> getPropertiesFromNestedClasses(DMP dmp, String location) {
        final List<Entity> properties = new ArrayList<>();
        // Grant
        if (getGrant_id() != null) {
            properties.addAll(getGrant_id().getProperties(dmp, getLocation(location)));
        }

        return properties;
    }
}
