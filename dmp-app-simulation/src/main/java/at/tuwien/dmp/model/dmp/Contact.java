package at.tuwien.dmp.model.dmp;

import javax.validation.constraints.NotNull;

public class Contact {

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
}
