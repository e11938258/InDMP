package at.tuwien.indmp.model.dmp;

import java.util.List;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.modul.PropertyModule;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.util.Functions;

public class Contact extends AbstractClassObject {

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
    public Object[] getValues() {
        return new Object[] {
                getMbox(),
                getName(),
        };
    }

    @Override
    public String[] getPropertyNames() {
        return new String[] {
                "mbox",
                "name",
        };
    }

    @Override
    public String getObjectIdentifier() {
        return getContact_id().getObjectIdentifier();
    }

    @Override
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String reference, RDMService rdmService) {
        return getContact_id().getProperties(dmp, reference, rdmService);
    }

    @Override
    public void build(PropertyModule propertyModule, String atLocation) {
        // ------------------------------------
        // Set properties
        // ------------------------------------
        final List<Property> properties = propertyModule.findEntities(atLocation, null, null, true);

        Property p = Functions.findPropertyInList(getObjectType(), "mbox", properties);
        setMbox(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getObjectType(), "name", properties);
        setName(p != null ? p.getValue() : null);

        // ------------------------------------
        // Set identifier
        // ------------------------------------
        final Property identifier = Functions.findPropertyInList(getObjectType(), "identifier", properties);
        final Property type = Functions.findPropertyInList(getObjectType(), "type", properties);
        contact_id = new Contact_id(identifier.getValue(), type.getValue());
    }
}
