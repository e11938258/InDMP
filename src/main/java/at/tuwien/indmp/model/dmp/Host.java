package at.tuwien.indmp.model.dmp;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.modul.PropertyModule;
import at.tuwien.indmp.util.ModelConstants;
import at.tuwien.indmp.util.Functions;

public class Host extends AbstractClassObject {

    /* Properties */
    private String availability;

    private String backup_frequency;

    private String backup_type;

    @Pattern(regexp = ModelConstants.REGEX_REPOSITORY_CERTIFIED)
    private String certified_with;

    private String description;

    @Pattern(regexp = ModelConstants.REGEX_ISO_3166_1)
    private String geo_location;

    @Pattern(regexp = ModelConstants.REGEX_PID_SYSTEM)
    private List<String> pid_system = new ArrayList<>();

    private String storage_type;

    @Pattern(regexp = ModelConstants.REGEX_YES_NO_UNKNOWN)
    private String support_versioning;

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

    public String getGeo_location() {
        return this.geo_location;
    }

    public void setGeo_location(String geo_location) {
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

    public String getSupport_versioning() {
        return this.support_versioning;
    }

    public void setSupport_versioning(String support_versioning) {
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
                getPid_system() != null ? getPid_system().toString() : null,
                getStorage_type(),
                getSupport_versioning(),
                getTitle(),
                getUrl().toString()
        };
    }

    @Override
    public String[] getPropertyNames() {
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
                "url"
        };
    }

    @Override
    public String getObjectIdentifier() {
        return getUrl().toString();
    }

    @Override
    public void build(PropertyModule propertyModule, String atLocation) {
        // ------------------------------------
        // Set properties
        // ------------------------------------
        final List<Property> properties = propertyModule.findEntities(atLocation, null, null, true);

        Property p = Functions.findPropertyInList(getObjectType(), "availability", properties);
        setAvailability(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getObjectType(), "backup_frequency", properties);
        setBackup_frequency(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getObjectType(), "backup_type", properties);
        setBackup_type(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getObjectType(), "certified_with", properties);
        setCertified_with(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getObjectType(), "description", properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getObjectType(), "geo_location", properties);
        setGeo_location(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getObjectType(), "pid_system", properties);
        setPid_system(p != null
                ? Arrays.asList(p.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",", -1))
                : null);

        p = Functions.findPropertyInList(getObjectType(), "storage_type", properties);
        setStorage_type(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getObjectType(), "support_versioning", properties);
        setSupport_versioning(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getObjectType(), "title", properties);
        setTitle(p != null ? p.getValue() : null);

        // ------------------------------------
        // Set identifier
        // ------------------------------------
        p = Functions.findPropertyInList(getObjectType(), "url", properties);
        setUrl(URI.create(p.getValue()));
    }
}
