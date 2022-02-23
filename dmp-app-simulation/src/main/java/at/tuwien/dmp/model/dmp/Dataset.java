package at.tuwien.dmp.model.dmp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.dmp.util.ModelConstants;

public class Dataset {

    /* Properties */
    private List<String> data_quality_assurance = new ArrayList<>();

    private String description;

    @JsonFormat(pattern = ModelConstants.DATE_FORMAT_ISO_8601)
    private Date issued;

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

    public Date getIssued() {
        return this.issued;
    }

    public void setIssued(Date issued) {
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
}
