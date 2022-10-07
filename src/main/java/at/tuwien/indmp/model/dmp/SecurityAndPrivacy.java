package at.tuwien.indmp.model.dmp;

import java.util.List;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.modul.PropertyModule;
import at.tuwien.indmp.util.Functions;

public class SecurityAndPrivacy extends AbstractClassObject {

    /* Properties */
    private String description;

    @NotNull
    private String title;

    public SecurityAndPrivacy() {
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getDescription(),
                getTitle()
        };
    }

    @Override
    public String[] getPropertyNames() {
        return new String[] {
                "description",
                "title"
        };
    }

    @Override
    public String getObjectIdentifier() {
        return getTitle();
    }

    @Override
    public void build(PropertyModule propertyModule, String atLocation) {
        // ------------------------------------
        // Set properties
        // ------------------------------------
        final List<Property> properties = propertyModule.findEntities(atLocation, null, null, true);

        Property p = Functions.findPropertyInList(getSpecializationOf("description"), properties);
        setDescription(p != null ? p.getValue() : null);

        // ------------------------------------
        // Set identifier
        // ------------------------------------
        p = Functions.findPropertyInList(getSpecializationOf("title"), properties);
        setTitle(p.getValue());
    }
}
