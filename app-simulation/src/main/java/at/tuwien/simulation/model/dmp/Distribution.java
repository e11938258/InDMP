package at.tuwien.simulation.model.dmp;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.simulation.model.Entity;
import at.tuwien.simulation.util.ModelConstants;

public class Distribution extends AbstractClassEntity {

    /* Properties */
    @NotNull
    private URI access_url;

    @JsonFormat(pattern = ModelConstants.DATE_FORMAT_ISO_8601)
    private Date available_until;

    private Long byte_size;

    @Pattern(regexp = ModelConstants.REGEX_DATA_ACCESS)
    private String data_access;

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

    public Distribution(URI access_url, Date available_until, Long byte_size, String data_access, String description,
            URI download_url, List<String> format, String title, Host host, List<License> license) {
        this.access_url = access_url;
        this.available_until = available_until;
        this.byte_size = byte_size;
        this.data_access = data_access;
        this.description = description;
        this.download_url = download_url;
        this.format = format;
        this.title = title;
        this.host = host;
        this.license = license;
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

    public String getData_access() {
        return this.data_access;
    }

    public void setData_access(String data_access) {
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
                getAvailable_until() != null ? ModelConstants.DATE_FORMATTER_ISO_8601.format(getAvailable_until())
                        : null,
                getByte_size(),
                getDescription(),
                getDownload_url(),
                getFormat().toString(),
                getTitle(),
                getAccess_url().toString()
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
                "access_url"
        };
    }

    @Override
    public String getClassIdentifier() {
        return getAccess_url().toString();
    }

    @Override
    public List<Entity> getPropertiesFromNestedClasses(DMP dmp, String location) {
        final List<Entity> properties = new ArrayList<>();

        // Host
        if (getHost() != null) {
            properties.addAll(getHost().getProperties(dmp, getLocation(location)));
        }

        // License
        for (License i : getLicense()) {
            properties.addAll(i.getProperties(dmp, getLocation(location)));
        }

        return properties;
    }
}
