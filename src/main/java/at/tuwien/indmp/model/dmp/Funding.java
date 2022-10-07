package at.tuwien.indmp.model.dmp;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.modul.PropertyModule;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.util.ModelConstants;
import at.tuwien.indmp.util.Functions;

public class Funding extends AbstractClassObject {

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
    public String[] getPropertyNames() {
        return new String[] {
                "funding_status",
        };
    }

    @Override
    public String getObjectIdentifier() {
        return getFunder_id().getObjectIdentifier();
    }

    @Override
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String atLocation, RDMService rdmService) {
        return getFunder_id().getProperties(dmp, atLocation, rdmService);
    }

    @Override
    public List<Property> getPropertiesFromNestedObjects(DMP dmp, String atLocation, RDMService rdmService) {
        final List<Property> properties = new ArrayList<>();
        
        // ------------------------------------
        // Nested object: Grant id
        // ------------------------------------
        if (getGrant_id() != null) {
            properties.addAll(getGrant_id().getProperties(dmp, getAtLocation(atLocation), rdmService));
        }

        return properties;
    }

    @Override
    public void build(PropertyModule propertyModule, String atLocation) {
        // ------------------------------------
        // Set properties
        // ------------------------------------
        final List<Property> properties = propertyModule.findEntities(atLocation, null, null, true);

        Property p = Functions.findPropertyInList(getObjectType(), "funding_status", properties);
        setFunding_status(p != null ? p.getValue() : null);

        // ------------------------------------
        // Set identifier
        // ------------------------------------
        Property identifier = Functions.findPropertyInList(getObjectType(), "identifier", properties);
        Property type = Functions.findPropertyInList(getObjectType(), "type", properties);
        funder_id = new Funder_id(identifier.getValue(), type.getValue());

        // ------------------------------------
        // Nested object: Set grant id
        // ------------------------------------
        for (Property property : propertyModule.findAllEntities(atLocation, "grant_id:identifier", true)) {
            // Set properties
            final List<Property> grantProperties = propertyModule.findEntities(atLocation + "/" + property.getValue(), null, null, true);

            // Set identifier
            identifier = Functions.findPropertyInList("grant_id", "identifier", grantProperties);
            type = Functions.findPropertyInList("grant_id", "type", grantProperties);

            // Set the grant id object
            setGrant_id(new Grant_id(identifier.getValue(), type.getValue()));
        }
    }
}
