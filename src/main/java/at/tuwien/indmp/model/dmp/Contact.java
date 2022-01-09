package at.tuwien.indmp.model.dmp;

import java.util.List;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.System;
import at.tuwien.indmp.service.PropertyService;
import at.tuwien.indmp.util.Functions;
import at.tuwien.indmp.util.dmp.PersonIdentifierType;
import at.tuwien.indmp.util.var.ServiceType;

public class Contact extends ClassEntity {

    /* Properties */
    @NotNull
    private String mbox;

    @NotNull
    private String name;

    /* Nested data structure */
    @NotNull
    private Contact_id contact_id;

    public Contact() {
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

    public Contact_id getContact_id() {
        return this.contact_id;
    }

    public void setContact_id(Contact_id contact_id) {
        this.contact_id = contact_id;
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
    public Object[] getValues() {
        return new Object[] {
                getMbox(),
                getName(),
        };
    }

    @Override
    public String[] getValueNames() {
        return new String[] {
                "mbox",
                "name",
        };
    }

    @Override
    public String getClassIdentifier() {
        return getContact_id().getClassIdentifier();
    }

    @Override
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String reference, System system) {
        return getContact_id().getProperties(dmp, reference, system);
    }

    @Override
    public List<Property> getPropertiesFromNestedClasses(DMP dmp, System system) {
        return null;
    }

    @Override
    public void build(PropertyService propertyService, String dmpIdentifier, String classIdentifier) {
        // Set properties
        final List<Property> properties = propertyService.findProperties(dmpIdentifier, "contributor", null,
                null, null, null);

        Property p = Functions.findPropertyInList("mbox", properties);
        setMbox(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("name", properties);
        setName(p != null ? p.getValue() : null);

        // Set identifier
        final List<Property> identifierProperties = propertyService.findProperties(dmpIdentifier, "contact_id",
                classIdentifier, null, null, null);
        final Property identifier = Functions.findPropertyInList("identifier", identifierProperties);
        final Property type = Functions.findPropertyInList("type", identifierProperties);
        contact_id = new Contact_id(identifier.getValue(), PersonIdentifierType.valueOf(type.getValue()));
    }
}