package at.tuwien.indmp.model.dmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.System;
import at.tuwien.indmp.service.PropertyService;
import at.tuwien.indmp.util.Functions;
import at.tuwien.indmp.util.dmp.PersonIdentifierType;
import at.tuwien.indmp.util.var.ServiceType;

public class Contributor extends ClassEntity {

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
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String reference, System system) {
        return getContributor_id().getProperties(dmp, reference, system);
    }

    @Override
    public List<Property> getPropertiesFromNestedClasses(DMP dmp, System system) {
        return null;
    }

    @Override
    public boolean hasRightsToUpdate(System system) {
        return Functions.isServiceTypeInArray(new ServiceType[] {
                ServiceType.DMP_APP,
                ServiceType.REPOSITORY_STORE,
                ServiceType.ADMINISTRATIVE_DATA_COLLECTOR,
                ServiceType.FUNDER_SYSTEM,
                ServiceType.IT_RESOURCE,
                ServiceType.REPOSITORY_INGESTOR,
        }, system.getType());
    }

    @Override
    public void build(PropertyService propertyService, String dmpIdentifier, String classIdentifier) {
        // Set properties
        final List<Property> properties = propertyService.findProperties(dmpIdentifier, "contributor", classIdentifier,
                null, null, null);

        Property p = Functions.findPropertyInList("mbox", properties);
        setMbox(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("name", properties);
        setName(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("role", properties);
        setRole(p != null
                ? Arrays.asList(p.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",", -1))
                : null);

        // Set identifier
        final List<Property> identifierProperties = propertyService.findProperties(dmpIdentifier, "contributor_id",
                classIdentifier, null, null, null);
        final Property identifier = Functions.findPropertyInList("identifier", identifierProperties);
        final Property type = Functions.findPropertyInList("type", identifierProperties);
        contributor_id = new Contributor_id(identifier.getValue(), PersonIdentifierType.valueOf(type.getValue()));
    }
}
