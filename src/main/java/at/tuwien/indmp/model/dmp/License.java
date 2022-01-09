package at.tuwien.indmp.model.dmp;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.System;
import at.tuwien.indmp.service.PropertyService;
import at.tuwien.indmp.util.Functions;
import at.tuwien.indmp.util.var.ServiceType;

public class License extends ClassEntity {

    public final static String DATE_FORMAT = "yyyy-MM-dd";
    public final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

    /* Properties */
    @NotNull
    private URI license_ref;

    @NotNull
    @JsonFormat(pattern = DATE_FORMAT)
    private Date start_date;

    public License() {
    }

    public URI getLicense_ref() {
        return this.license_ref;
    }

    public void setLicense_ref(URI license_ref) {
        this.license_ref = license_ref;
    }

    public Date getStart_date() {
        return this.start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getStart_date() != null ? DATE_FORMATTER.format(getStart_date()) : null,
        };
    }

    @Override
    public String[] getValueNames() {
        return new String[] {
                "start_date",
        };
    }

    @Override
    public String getClassIdentifier() {
        return getLicense_ref().toString();
    }

    @Override
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String reference, System system) {
        final List<Property> properties = new ArrayList<>();

        final Property property = new Property(dmp.getClassIdentifier(), getClassName(), getClassIdentifier(), "license_ref",
        getClassIdentifier(), reference, dmp.getModified(), system);
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
                ServiceType.REPOSITORY_STORE,
                ServiceType.REPOSITORY_INGESTOR,
        }, system.getType());
    }

    @Override
    public void build(PropertyService propertyService, String dmpIdentifier, String classIdentifier) {
        // Set properties
        final List<Property> properties = propertyService.findProperties(dmpIdentifier, "license", classIdentifier,
                null, null, null);

        Property p = Functions.findPropertyInList("start_date", properties);
        try {
            setStart_date(p != null ? DATE_FORMATTER.parse(p.getValue()) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Set identifier
        p = Functions.findPropertyInList("license_ref", properties);
        setLicense_ref(URI.create(p.getValue()));
    }
}
