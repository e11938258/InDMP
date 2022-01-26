package at.tuwien.indmp.model.dmp;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.service.PropertyService;
import at.tuwien.indmp.util.DMPConstants;
import at.tuwien.indmp.util.Functions;

public class Funding extends ClassEntity {

    /* Properties */
    @Pattern(regexp = DMPConstants.REGEX_FUNDING_STATUS)
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
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String reference, RDMService system) {
        return getFunder_id().getProperties(dmp, reference, system);
    }

    @Override
    public List<Property> getPropertiesFromNestedClasses(DMP dmp, RDMService system) {
        final List<Property> properties = new ArrayList<>();
        // Grant
        if (getGrant_id() != null) {
            properties.addAll(getGrant_id().getProperties(dmp, getClassIdentifier(), system));
        }

        return properties;
    }

    @Override
    public void build(PropertyService propertyService, String dmpIdentifier, String classIdentifier) {
        // Set properties
        final List<Property> properties = propertyService.findProperties(dmpIdentifier, getClassType(), classIdentifier,
                null, null, null);

        Property p = Functions.findPropertyInList("funding_status", properties);
        setFunding_status(p != null ? p.getValue() : null);

        // Set identifier
        final List<Property> identifierProperties = propertyService.findProperties(dmpIdentifier, "funder_id",
                classIdentifier, null, null, null);
        Property identifier = Functions.findPropertyInList("identifier", identifierProperties);
        Property type = Functions.findPropertyInList("type", identifierProperties);
        funder_id = new Funder_id(identifier.getValue(), type.getValue());

        // Set grant id
        final List<Property> grantProperties = propertyService.findProperties(dmpIdentifier, "grant_id",
                null, null, null, classIdentifier);
        if (grantProperties.size() >= 2) {
            identifier = Functions.findPropertyInList("identifier", identifierProperties);
            type = Functions.findPropertyInList("type", identifierProperties);
            grant_id = new Grant_id(identifier.getValue(), type.getValue());
        }
    }
}
