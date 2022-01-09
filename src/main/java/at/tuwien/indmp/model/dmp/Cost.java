package at.tuwien.indmp.model.dmp;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.System;
import at.tuwien.indmp.service.PropertyService;
import at.tuwien.indmp.util.Functions;
import at.tuwien.indmp.util.dmp.CurrencyCode;
import at.tuwien.indmp.util.var.ServiceType;

public class Cost extends ClassEntity {

    /* Properties */
    private CurrencyCode currency_code;

    private String description;

    @NotNull
    private String title;

    private Double value;

    public Cost() {
    }

    public CurrencyCode getCurrency_code() {
        return this.currency_code;
    }

    public void setCurrency_code(CurrencyCode currency_code) {
        this.currency_code = currency_code;
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

    public Double getValue() {
        return this.value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getCurrency_code(),
                getDescription(),
                getValue(),
        };
    }

    @Override
    public String[] getValueNames() {
        return new String[] {
                "currency_code",
                "description",
                "value",
        };
    }

    @Override
    public String getClassIdentifier() {
        return getTitle();
    }

    @Override
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String reference, System system) {
        final List<Property> properties = new ArrayList<>();

        final Property property = new Property(dmp.getClassIdentifier(), getClassName(), getClassIdentifier(),
        "title", getTitle(), reference, dmp.getModified(), system);
        properties.add(property);
        system.add(property);

        return properties;
    }

    @Override
    public List<Property> getPropertiesFromNestedClasses(DMP dmp, System system) {
        return null;
    }

    @Override
    public boolean hasRightsToUpdate(System system) {
        return Functions.isServiceTypeInArray(new ServiceType[] {
                ServiceType.DMP_APP,
                ServiceType.ADMINISTRATIVE_DATA_COLLECTOR,
                ServiceType.FUNDER_SYSTEM,
        }, system.getType());
    }

    @Override
    public void build(PropertyService propertyService, String dmpIdentifier, String classIdentifier) {
        // Set properties
        final List<Property> properties = propertyService.findProperties(dmpIdentifier, "cost", classIdentifier, null,
                null, null);

        Property p = Functions.findPropertyInList("currency_code", properties);
        setCurrency_code(p != null ? CurrencyCode.valueOf(p.getValue()) : null);

        p = Functions.findPropertyInList("description", properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("value", properties);
        setValue(p != null ? Double.parseDouble(p.getValue()) : null);

    }
}
