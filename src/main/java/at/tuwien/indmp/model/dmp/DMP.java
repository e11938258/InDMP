package at.tuwien.indmp.model.dmp;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.modul.PropertyModule;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.util.ModelConstants;
import at.tuwien.indmp.util.Functions;

public class DMP extends AbstractClassObject {

    /* Properties */
    @NotNull
    @JsonFormat(pattern = ModelConstants.DATE_TIME_FORMAT_ISO_8601)
    private LocalDateTime created;

    private String description;

    private String ethical_issues_description;

    @Pattern(regexp = ModelConstants.REGEX_YES_NO_UNKNOWN)
    private String ethical_issues_exist;

    private URI ethical_issues_report;

    @Pattern(regexp = ModelConstants.REGEX_ISO_639_3)
    private String language;

    @NotNull
    @JsonFormat(pattern = ModelConstants.DATE_TIME_FORMAT_ISO_8601)
    private LocalDateTime modified;

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
     * Mandatory maDMP properties
     * 
     * @param created
     * @param modified
     * @param dmp_id
     */
    public DMP(LocalDateTime created, LocalDateTime modified, DMP_id dmp_id) {
        this.created = created;
        this.modified = modified;
        this.dmp_id = dmp_id;
    }

    public LocalDateTime getCreated() {
        return this.created;
    }

    public void setCreated(LocalDateTime created) {
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

    public LocalDateTime getModified() {
        return this.modified;
    }

    public void setModified(LocalDateTime modified) {
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

    @JsonIgnore
    public boolean isNew() {
        return getCreated().equals(getModified());
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                ModelConstants.DATE_TIME_FORMATTER_ISO_8601.format(getCreated()),
                getDescription(),
                getEthical_issues_description(),
                getEthical_issues_exist(),
                getEthical_issues_report(),
                getLanguage(),
                getTitle(),
        };
    }

    @Override
    public String[] getPropertyNames() {
        return new String[] {
                "created",
                "description",
                "ethical_issues_description",
                "ethical_issues_exist",
                "ethical_issues_report",
                "language",
                "title",
        };
    }

    @Override
    public String getObjectIdentifier() {
        return getDmp_id().getObjectIdentifier();
    }

    @Override
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String atLocation, RDMService rdmService) {
        return dmp_id.getProperties(dmp, atLocation, rdmService);
    }

    @Override
    public List<Property> getPropertiesFromNestedObjects(DMP dmp, String atLocation, RDMService rdmService) {
        final List<Property> properties = new ArrayList<>();

        // ------------------------------------
        // Nested object: Contact
        // ------------------------------------
        if (getContact() != null) {
            properties.addAll(getContact().getProperties(dmp, getAtLocation(atLocation), rdmService));
        }

        // ------------------------------------
        // Nested object: Contributor
        // ------------------------------------
        for (Contributor i : getContributor()) {
            properties.addAll(i.getProperties(dmp, getAtLocation(atLocation), rdmService));
        }

        // ------------------------------------
        // Nested object: Cost
        // ------------------------------------
        for (Cost i : getCost()) {
            properties.addAll(i.getProperties(dmp, getAtLocation(atLocation), rdmService));
        }

        // ------------------------------------
        // Nested object: Dataset
        // ------------------------------------
        for (Dataset i : getDataset()) {
            properties.addAll(i.getProperties(dmp, getAtLocation(atLocation), rdmService));
            properties.addAll(i.getPropertiesFromNestedObjects(dmp, getAtLocation(atLocation), rdmService));
        }

        // ------------------------------------
        // Nested object: Project
        // ------------------------------------
        for (Project i : getProject()) {
            properties.addAll(i.getProperties(dmp, getAtLocation(atLocation), rdmService));
            properties.addAll(i.getPropertiesFromNestedObjects(dmp, getAtLocation(atLocation), rdmService));
        }

        return properties;
    }

    @JsonIgnore
    @Override
    public List<Property> getProperties(DMP dmp, String atLocation, RDMService rdmService) {
        final List<Property> properties = super.getProperties(dmp, atLocation, rdmService);

        // ------------------------------------
        // Add modified date every time
        // ------------------------------------
        properties.add(Functions.propertyMaker(dmp, getAtLocation(atLocation), getObjectType() + ":modified",
                ModelConstants.DATE_TIME_FORMATTER_ISO_8601.format(getModified())));

        return properties;
    }

    @Override
    public void build(PropertyModule propertyModule, String atLocation) {
        // ------------------------------------
        // Set properties
        // ------------------------------------
        final List<Property> properties = propertyModule.findProperties(atLocation, null, null, true);

        Property p = Functions.findPropertyInList(getSpecializationOf("created"), properties);
        setCreated(p != null ? LocalDateTime.parse(p.getValue()) : null);

        p = Functions.findPropertyInList(getSpecializationOf("description"), properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getSpecializationOf("ethical_issues_description"), properties);
        setEthical_issues_description(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getSpecializationOf("ethical_issues_exist"), properties);
        setEthical_issues_exist(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getSpecializationOf("ethical_issues_report"), properties);
        setEthical_issues_report(p != null ? URI.create(p.getValue()) : null);

        p = Functions.findPropertyInList(getSpecializationOf("language"), properties);
        setLanguage(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getSpecializationOf("modified"), properties);
        setModified(p != null ? LocalDateTime.parse(p.getValue()) : null);

        p = Functions.findPropertyInList(getSpecializationOf("title"), properties);
        setTitle(p != null ? p.getValue() : null);

        // ------------------------------------
        // Set identifier
        // ------------------------------------
        final Property identifier = Functions.findPropertyInList(getSpecializationOf("identifier"), properties);
        final Property type = Functions.findPropertyInList(getSpecializationOf("type"), properties);
        dmp_id = new DMP_id(identifier.getValue());
        if (type != null) {
            dmp_id.setType(type.getValue());
        }

        // ------------------------------------
        // Nested object: Build contact
        // ------------------------------------
        for (Property property : propertyModule.findAllProperties(atLocation, "contact:identifier", true)) {
            contact = new Contact();
            contact.build(propertyModule, atLocation + "/" + property.getValue());
        }

        // ------------------------------------
        // Nested object: Build contributor
        // ------------------------------------
        for (Property property : propertyModule.findAllProperties(atLocation, "contributor:identifier", true)) {
            final Contributor i = new Contributor();
            i.build(propertyModule, atLocation + "/" + property.getValue());
            contributor.add(i);
        }

        // ------------------------------------
        // Nested object: Build cost
        // ------------------------------------
        for (Property property : propertyModule.findAllProperties(atLocation, "cost:title", true)) {
            final Cost i = new Cost();
            i.build(propertyModule, atLocation + "/" + property.getValue());
            cost.add(i);
        }

        // ------------------------------------
        // Nested object: Build project
        // ------------------------------------
        for (Property property : propertyModule.findAllProperties(atLocation, "project:title", true)) {
            final Project i = new Project();
            i.build(propertyModule, atLocation + "/" + property.getValue());
            project.add(i);
        }

        // ------------------------------------
        // Nested object: Build dataset
        // ------------------------------------
        for (Property property : propertyModule.findAllProperties(atLocation, "dataset:identifier", true)) {
            final Dataset i = new Dataset();
            i.build(propertyModule, atLocation + "/" + property.getValue());
            dataset.add(i);
        }

    }
}
