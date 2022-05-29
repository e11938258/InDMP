package at.tuwien.indmp.model.dmp;

import java.util.List;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.model.DataService;
import at.tuwien.indmp.model.Entity;
import at.tuwien.indmp.service.EntityService;
import at.tuwien.indmp.util.Functions;

public class Contact extends AbstractClassEntity {

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
    public List<Entity> getPropertiesFromIdentifier(DMP dmp, String reference, DataService dataService) {
        return getContact_id().getProperties(dmp, reference, dataService);
    }

    @Override
    public void build(EntityService entityService, String location) {
        // Set properties
        final List<Entity> properties = entityService.findEntities(location, null, null, true);

        Entity p = Functions.findPropertyInList(getClassType(), "mbox", properties);
        setMbox(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getClassType(), "name", properties);
        setName(p != null ? p.getValue() : null);

        // Set identifier
        final Entity identifier = Functions.findPropertyInList(getClassType(), "identifier", properties);
        final Entity type = Functions.findPropertyInList(getClassType(), "type", properties);
        contact_id = new Contact_id(identifier.getValue(), type.getValue());
    }
}
