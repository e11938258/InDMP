package at.tuwien.simulation.model.dmp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.simulation.model.Entity;
import at.tuwien.simulation.util.ModelConstants;

public class Dataset extends AbstractClassEntity {

    /* Properties */
    private List<String> data_quality_assurance = new ArrayList<>();

    private String description;

    @JsonFormat(pattern = ModelConstants.DATE_FORMAT_ISO_8601)
    private LocalDate issued;

    private List<String> keyword = new ArrayList<>();

    @Pattern(regexp = ModelConstants.REGEX_ISO_639_3)
    private String language;

    @NotNull
    @Pattern(regexp = ModelConstants.REGEX_YES_NO_UNKNOWN)
    private String personal_data;

    private String preservation_statement;

    @NotNull
    @Pattern(regexp = ModelConstants.REGEX_YES_NO_UNKNOWN)
    private String sensitive_data;

    @NotNull
    private String title;

    private String type;

    /* Nested data structure */
    @NotNull
    private Dataset_id dataset_id;

    private List<Distribution> distribution = new ArrayList<>();

    private List<Metadata> metadata = new ArrayList<>();

    private List<SecurityAndPrivacy> security_and_privacy = new ArrayList<>();

    private List<TechnicalResource> technical_resource = new ArrayList<>();

    public Dataset() {
    }

    public Dataset(List<String> data_quality_assurance, String description, LocalDate issued, List<String> keyword,
            String language, String personal_data, String preservation_statement, String sensitive_data, String title,
            String type, Dataset_id dataset_id, List<Distribution> distribution, List<Metadata> metadata,
            List<SecurityAndPrivacy> security_and_privacy, List<TechnicalResource> technical_resource) {
        this.data_quality_assurance = data_quality_assurance;
        this.description = description;
        this.issued = issued;
        this.keyword = keyword;
        this.language = language;
        this.personal_data = personal_data;
        this.preservation_statement = preservation_statement;
        this.sensitive_data = sensitive_data;
        this.title = title;
        this.type = type;
        this.dataset_id = dataset_id;
        this.distribution = distribution;
        this.metadata = metadata;
        this.security_and_privacy = security_and_privacy;
        this.technical_resource = technical_resource;
    }

    public List<String> getData_quality_assurance() {
        return this.data_quality_assurance;
    }

    public void setData_quality_assurance(List<String> data_quality_assurance) {
        this.data_quality_assurance = data_quality_assurance;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getIssued() {
        return this.issued;
    }

    public void setIssued(LocalDate issued) {
        this.issued = issued;
    }

    public List<String> getKeyword() {
        return this.keyword;
    }

    public void setKeyword(List<String> keyword) {
        this.keyword = keyword;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPersonal_data() {
        return this.personal_data;
    }

    public void setPersonal_data(String personal_data) {
        this.personal_data = personal_data;
    }

    public String getPreservation_statement() {
        return this.preservation_statement;
    }

    public void setPreservation_statement(String preservation_statement) {
        this.preservation_statement = preservation_statement;
    }

    public String getSensitive_data() {
        return this.sensitive_data;
    }

    public void setSensitive_data(String sensitive_data) {
        this.sensitive_data = sensitive_data;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Dataset_id getDataset_id() {
        return this.dataset_id;
    }

    public void setDataset_id(Dataset_id dataset_id) {
        this.dataset_id = dataset_id;
    }

    public List<Distribution> getDistribution() {
        return this.distribution;
    }

    public void setDistribution(List<Distribution> distribution) {
        this.distribution = distribution;
    }

    public List<Metadata> getMetadata() {
        return this.metadata;
    }

    public void setMetadata(List<Metadata> metadata) {
        this.metadata = metadata;
    }

    public List<SecurityAndPrivacy> getSecurity_and_privacy() {
        return this.security_and_privacy;
    }

    public void setSecurity_and_privacy(List<SecurityAndPrivacy> security_and_privacy) {
        this.security_and_privacy = security_and_privacy;
    }

    public List<TechnicalResource> getTechnical_resource() {
        return this.technical_resource;
    }

    public void setTechnical_resource(List<TechnicalResource> technical_resource) {
        this.technical_resource = technical_resource;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getData_quality_assurance() != null ? getData_quality_assurance().toString() : null,
                getDescription(),
                getIssued() != null ? ModelConstants.DATE_FORMATTER_ISO_8601.format(getIssued()) : null,
                getKeyword() != null ? getKeyword().toString() : null,
                getLanguage(),
                getPersonal_data(),
                getPreservation_statement(),
                getSensitive_data(),
                getTitle(),
                getType(),
        };
    }

    @Override
    public String[] getValueNames() {
        return new String[] {
                "data_quality_assurance",
                "description",
                "issued",
                "keyword",
                "language",
                "personal_data",
                "preservation_statement",
                "sensitive_data",
                "title",
                "dtype",
        };
    }

    @Override
    public String getClassIdentifier() {
        return getDataset_id().getClassIdentifier();
    }

    @Override
    public List<Entity> getPropertiesFromIdentifier(DMP dmp, String location) {
        return getDataset_id().getProperties(dmp, location);
    }

    @Override
    public List<Entity> getPropertiesFromNestedClasses(DMP dmp, String location) {
        final List<Entity> properties = new ArrayList<>();

        // Distribution
        for (Distribution i : getDistribution()) {
            properties.addAll(i.getProperties(dmp, getLocation(location)));
            properties.addAll(i.getPropertiesFromNestedClasses(dmp, getLocation(location)));
        }
        // Metadata
        for (Metadata i : getMetadata()) {
            properties.addAll(i.getProperties(dmp, getLocation(location)));
        }
        // Security and privacy
        for (SecurityAndPrivacy i : getSecurity_and_privacy()) {
            properties.addAll(i.getProperties(dmp, getLocation(location)));
        }
        // Technical resource
        for (TechnicalResource i : getTechnical_resource()) {
            properties.addAll(i.getProperties(dmp, getLocation(location)));
        }

        return properties;
    }
}
