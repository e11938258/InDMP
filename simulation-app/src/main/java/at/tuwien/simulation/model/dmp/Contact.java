package at.tuwien.simulation.model.dmp;

import java.util.List;

import javax.validation.constraints.NotNull;

import at.tuwien.simulation.model.Entity;

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

    public Contact(String mbox, String name, Contact_id contact_id) {
        this.mbox = mbox;
        this.name = name;
        this.contact_id = contact_id;
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
    public List<Entity> getPropertiesFromIdentifier(DMP dmp, String reference) {
        return getContact_id().getProperties(dmp, reference);
    }
}
