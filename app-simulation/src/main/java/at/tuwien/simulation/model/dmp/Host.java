package at.tuwien.simulation.model.dmp;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.simulation.util.ModelConstants;

public class Host extends AbstractClassEntity {

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
                getPid_system(),
                getStorage_type(),
                getSupport_versioning(),
                getTitle(),
                getUrl().toString()
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
                "url"
        };
    }

    @Override
    public String getClassIdentifier() {
        return getUrl().toString();
    }
}
