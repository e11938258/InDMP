package at.tuwien.indmp.model.dmp;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.System;
import at.tuwien.indmp.service.PropertyService;
import at.tuwien.indmp.util.Functions;
import at.tuwien.indmp.util.dmp.DataAccess;
import at.tuwien.indmp.util.var.ServiceType;

public class Distribution extends ClassEntity {

    public final static String DATE_FORMAT = "yyyy-MM-dd";
    public final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

    /* Properties */
    @NotNull
    private URI access_url;

    @JsonFormat(pattern = DATE_FORMAT)
    private Date available_until;

    private Long byte_size;

    private DataAccess data_access;

    private String description;

    private URI download_url;

    private List<String> format = new ArrayList<>();

    @NotNull
    private String title;

    /* Nested data structure */
    private Host host;

    private List<License> license = new ArrayList<>();

    public Distribution() {
    }

    public URI getAccess_url() {
        return this.access_url;
    }

    public void setAccess_url(URI access_url) {
        this.access_url = access_url;
    }

    public Date getAvailable_until() {
        return this.available_until;
    }

    public void setAvailable_until(Date available_until) {
        this.available_until = available_until;
    }

    public Long getByte_size() {
        return this.byte_size;
    }

    public void setByte_size(Long byte_size) {
        this.byte_size = byte_size;
    }

    public DataAccess getData_access() {
        return this.data_access;
    }

    public void setData_access(DataAccess data_access) {
        this.data_access = data_access;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URI getDownload_url() {
        return this.download_url;
    }

    public void setDownload_url(URI download_url) {
        this.download_url = download_url;
    }

    public List<String> getFormat() {
        return this.format;
    }

    public void setFormat(List<String> format) {
        this.format = format;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Host getHost() {
        return this.host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public List<License> getLicense() {
        return this.license;
    }

    public void setLicense(List<License> license) {
        this.license = license;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getData_access(),
                getAvailable_until() != null ? DATE_FORMATTER.format(getAvailable_until()) : null,
                getByte_size(),
                getDescription(),
                getDownload_url(),
                getFormat().toString(),
                getTitle(),
        };
    }

    @Override
    public String[] getValueNames() {
        return new String[] {
                "data_access",
                "available_until",
                "byte_size",
                "description",
                "download_url",
                "format",
                "title",
        };
    }

    @Override
    public String getClassIdentifier() {
        return getAccess_url().toString();
    }

    @Override
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String reference, System system) {
        final List<Property> properties = new ArrayList<>();

        final Property property = new Property(dmp.getClassIdentifier(), getClassName(), getClassIdentifier(), "access_url",
        getClassIdentifier(), reference, dmp.getModified(), system);
        properties.add(property);
        system.add(property);

        return properties;
    }

    @Override
    public List<Property> getPropertiesFromNestedClasses(DMP dmp, System system) {
        final List<Property> properties = new ArrayList<>();

        // Host
        if (getHost() != null) {
            properties.addAll(getHost().getProperties(dmp, getClassIdentifier(), system));
        }

        // License
        for (License i : getLicense()) {
            properties.addAll(i.getProperties(dmp, getClassIdentifier(), system));
        }

        return properties;
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
        // Nested classes
        // Host
        final Property hostIdentifier = propertyService.findProperty(dmpIdentifier, "host", null, "url", null,
                classIdentifier);
        if (hostIdentifier != null) {
            host = new Host();
            host.build(propertyService, dmpIdentifier, hostIdentifier.getValue());
        }

        // License
        for (Property property : propertyService.findProperties(dmpIdentifier, "license", null, "license_ref", null,
                classIdentifier)) {
            final License i = new License();
            i.build(propertyService, dmpIdentifier, property.getValue());
            license.add(i);
        }

        // Set properties
        final List<Property> properties = propertyService.findProperties(dmpIdentifier, "distribution", classIdentifier,
                null, null, null);

        Property p = Functions.findPropertyInList("data_access", properties);
        setData_access(p != null ? DataAccess.valueOf(p.getValue()) : null);

        p = Functions.findPropertyInList("available_until", properties);
        try {
            setAvailable_until(p != null ? DATE_FORMATTER.parse(p.getValue()) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        p = Functions.findPropertyInList("byte_size", properties);
        setByte_size(p != null ? Long.valueOf(p.getValue()) : null);

        p = Functions.findPropertyInList("description", properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("download_url", properties);
        setDownload_url(p != null ? URI.create(p.getValue()) : null);

        p = Functions.findPropertyInList("format", properties);
        setFormat(p != null
                ? Arrays.asList(p.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",", -1))
                : null);

        p = Functions.findPropertyInList("title", properties);
        setTitle(p != null ? p.getValue() : null);

        // Set identifier
        p = Functions.findPropertyInList("access_url", properties);
        setAccess_url(URI.create(p.getValue()));
    }
}
