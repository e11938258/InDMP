package at.tuwien.dmp.model.dmp;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import at.tuwien.dmp.util.ModelConstants;

public class DMP {

    /* Properties */
    @NotNull
    @JsonFormat(pattern = ModelConstants.DATE_TIME_FORMAT_ISO_8601)
    private Date created;

    private String description;

    private String ethical_issues_description;

    @Pattern(regexp = ModelConstants.REGEX_YES_NO_UNKNOWN)
    private String ethical_issues_exist;

    private URI ethical_issues_report;

    @Pattern(regexp = ModelConstants.REGEX_ISO_639_3)
    private String language;

    @NotNull
    @JsonFormat(pattern = ModelConstants.DATE_TIME_FORMAT_ISO_8601)
    private Date modified;

    private String title;

    /* Nested data structure */
    private Contact contact;

    private List<Contributor> contributor = new ArrayList<>();

    private List<Cost> cost = new ArrayList<>();

    private List<Dataset> dataset = new ArrayList<>();

    @NotNull
    private DMP_id dmp_id;

    private List<Project> project = new ArrayList<>();

    public DMP() {
    }

    /**
     * 
     * Minimal maDMP
     * 
     * @param created
     * @param modified
     * @param dmp_id
     */
    public DMP(Date created, Date modified, DMP_id dmp_id) {
        this.created = created;
        this.modified = modified;
        this.dmp_id = dmp_id;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @JsonIgnore
    public String getCreatedInString() {
        return ModelConstants.DATE_TIME_FORMATTER_ISO_8601.format(this.created);
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEthical_issues_description() {
        return this.ethical_issues_description;
    }

    public void setEthical_issues_description(String ethical_issues_description) {
        this.ethical_issues_description = ethical_issues_description;
    }

    public String getEthical_issues_exist() {
        return this.ethical_issues_exist;
    }

    public void setEthical_issues_exist(String ethical_issues_exist) {
        this.ethical_issues_exist = ethical_issues_exist;
    }

    public URI getEthical_issues_report() {
        return this.ethical_issues_report;
    }

    public void setEthical_issues_report(URI ethical_issues_report) {
        this.ethical_issues_report = ethical_issues_report;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Date getModified() {
        return this.modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public List<Contributor> getContributor() {
        return this.contributor;
    }

    public void setContributor(List<Contributor> contributor) {
        this.contributor = contributor;
    }

    public List<Cost> getCost() {
        return this.cost;
    }

    public void setCost(List<Cost> cost) {
        this.cost = cost;
    }

    public List<Dataset> getDataset() {
        return this.dataset;
    }

    public void setDataset(List<Dataset> dataset) {
        this.dataset = dataset;
    }

    public DMP_id getDmp_id() {
        return this.dmp_id;
    }

    public void setDmp_id(DMP_id dmp_id) {
        this.dmp_id = dmp_id;
    }

    public List<Project> getProject() {
        return this.project;
    }

    public void setProject(List<Project> project) {
        this.project = project;
    }
}
