package at.tuwien.indmp.model.dmp;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.System;
import at.tuwien.indmp.service.PropertyService;
import at.tuwien.indmp.util.Functions;
import at.tuwien.indmp.util.dmp.AllowedValues;
import at.tuwien.indmp.util.dmp.CountryCode;
import at.tuwien.indmp.util.var.ServiceType;

public class Host extends ClassEntity {

    /* Properties */
    private String availability;

    private String backup_frequency;

    private String backup_type;

    // TODO: Restrict the values
    private String certified_with;

    private String description;

    private CountryCode geo_location;

    // TODO: Restrict the values
    private List<String> pid_system = new ArrayList<>();

    private String storage_type;

    private AllowedValues support_versioning;

    @NotNull
    private String title;

    @NotNull
    private URI url;

    public Host() {
    }

    public String getAvailability() {
        return this.availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getBackup_frequency() {
        return this.backup_frequency;
    }

    public void setBackup_frequency(String backup_frequency) {
        this.backup_frequency = backup_frequency;
    }

    public String getBackup_type() {
        return this.backup_type;
    }

    public void setBackup_type(String backup_type) {
        this.backup_type = backup_type;
    }

    public String getCertified_with() {
        return this.certified_with;
    }

    public void setCertified_with(String certified_with) {
        this.certified_with = certified_with;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CountryCode getGeo_location() {
        return this.geo_location;
    }

    public void setGeo_location(CountryCode geo_location) {
        this.geo_location = geo_location;
    }

    public List<String> getPid_system() {
        return this.pid_system;
    }

    public void setPid_system(List<String> pid_system) {
        this.pid_system = pid_system;
    }

    public String getStorage_type() {
        return this.storage_type;
    }

    public void setStorage_type(String storage_type) {
        this.storage_type = storage_type;
    }

    public AllowedValues getSupport_versioning() {
        return this.support_versioning;
    }

    public void setSupport_versioning(AllowedValues support_versioning) {
        this.support_versioning = support_versioning;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URI getUrl() {
        return this.url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getAvailability(),
                getBackup_frequency(),
                getBackup_type(),
                getCertified_with(),
                getDescription(),
                getGeo_location(),
                getPid_system(),
                getStorage_type(),
                getSupport_versioning(),
                getTitle(),
        };
    }

    @Override
    public String[] getValueNames() {
        return new String[] {
                "availability",
                "backup_frequency",
                "backup_type",
                "certified_with",
                "description",
                "geo_location",
                "pid_system",
                "storage_type",
                "support_versioning",
                "title",
        };
    }

    @Override
    public String getClassIdentifier() {
        return getUrl().toString();
    }

    @Override
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String reference, System system) {
        final List<Property> properties = new ArrayList<>();

        final Property property = new Property(dmp.getClassIdentifier(), getClassName(), getClassIdentifier(), "url",
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
        final List<Property> properties = propertyService.findProperties(dmpIdentifier, "host", classIdentifier,
                null, null, null);

        Property p = Functions.findPropertyInList("availability", properties);
        setAvailability(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("backup_frequency", properties);
        setBackup_frequency(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("backup_type", properties);
        setBackup_type(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("certified_with", properties);
        setCertified_with(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("description", properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("geo_location", properties);
        setGeo_location(p != null ? CountryCode.valueOf(p.getValue()) : null);

        p = Functions.findPropertyInList("pid_system", properties);
        setPid_system(p != null
                ? Arrays.asList(p.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",", -1))
                : null);

        p = Functions.findPropertyInList("storage_type", properties);
        setStorage_type(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("support_versioning", properties);
        setSupport_versioning(p != null ? AllowedValues.valueOf(p.getValue()) : null);

        p = Functions.findPropertyInList("title", properties);
        setTitle(p != null ? p.getValue() : null);

        // Set identifier
        p = Functions.findPropertyInList("url", properties);
        setUrl(URI.create(p.getValue()));
    }
}
