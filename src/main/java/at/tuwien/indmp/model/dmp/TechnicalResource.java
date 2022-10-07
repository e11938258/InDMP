package at.tuwien.indmp.model.dmp;

import java.util.List;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.modul.PropertyModule;
import at.tuwien.indmp.util.Functions;

public class TechnicalResource extends AbstractClassObject {

    /* Properties */
    private String description;

    @NotNull
    private String name;

    public TechnicalResource() {
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getDescription(),
                getName()
        };
    }

    @Override
    public String[] getPropertyNames() {
        return new String[] {
                "description",
                "name"
        };
    }

    @Override
    public String getObjectIdentifier() {
        return getName();
    }

    @Override
    public void build(PropertyModule propertyModule, String atLocation) {
        // ------------------------------------
        // Set properties
        // ------------------------------------
        final List<Property> properties = propertyModule.findEntities(atLocation, null, null, true);

        Property p = Functions.findPropertyInList(getObjectType(), "description", properties);
        setDescription(p != null ? p.getValue() : null);

        // ------------------------------------
        // Set identifier
        // ------------------------------------
        p = Functions.findPropertyInList(getObjectType(),"name", properties);
        setName(p.getValue());
    }
}
