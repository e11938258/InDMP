package at.tuwien.indmp.model.dmp;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import at.tuwien.indmp.exception.BadRequestException;
import at.tuwien.indmp.model.DataService;
import at.tuwien.indmp.model.Entity;
import at.tuwien.indmp.service.EntityService;
import at.tuwien.indmp.util.ModelConstants;
import at.tuwien.indmp.util.Functions;

public class DMP extends AbstractClassEntity {

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

    /**
     * 
     * Minimal maDMP
     * 
     * @param created
     * @param modified
     * @param dmp_id
     */
    public DMP(String created, String modified, DMP_id dmp_id) {
        try {
            this.created = ModelConstants.DATE_TIME_FORMATTER_ISO_8601.parse(created);
            this.modified = ModelConstants.DATE_TIME_FORMATTER_ISO_8601.parse(modified);
        } catch (ParseException e) {
            throw new BadRequestException("Wrong date format");
        }
        this.dmp_id = dmp_id;
    }

    public DMP(String created, Date modified, DMP_id dmp_id) {
        try {
            this.created = ModelConstants.DATE_TIME_FORMATTER_ISO_8601.parse(created);
        } catch (ParseException e) {
            throw new BadRequestException("Wrong date format");
        }
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
    public String[] getValueNames() {
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
    public String getClassIdentifier() {
        return getDmp_id().getClassIdentifier();
    }

    @Override
    public List<Entity> getPropertiesFromIdentifier(DMP dmp, String location, DataService dataService) {
        return dmp_id.getProperties(dmp, location, dataService);
    }

    @Override
    public List<Entity> getPropertiesFromNestedClasses(DMP dmp, String location, DataService dataService) {
        final List<Entity> properties = new ArrayList<>();

        // Contact
        if (getContact() != null) {
            properties.addAll(getContact().getProperties(dmp, getLocation(location), dataService));
        }
        // Contributor
        for (Contributor i : getContributor()) {
            properties.addAll(i.getProperties(dmp, getLocation(location), dataService));
        }
        // Cost
        for (Cost i : getCost()) {
            properties.addAll(i.getProperties(dmp, getLocation(location), dataService));
        }
        // Dataset
        for (Dataset i : getDataset()) {
            properties.addAll(i.getProperties(dmp, getLocation(location), dataService));
            properties.addAll(i.getPropertiesFromNestedClasses(dmp, getLocation(location), dataService));
        }
        // Project
        for (Project i : getProject()) {
            properties.addAll(i.getProperties(dmp, getLocation(location), dataService));
            properties.addAll(i.getPropertiesFromNestedClasses(dmp, getLocation(location), dataService));
        }

        return properties;
    }

    @JsonIgnore
    @Override
    public List<Entity> getProperties(DMP dmp, String location, DataService dataService) {
        final List<Entity> properties = super.getProperties(dmp, location, dataService);
        // Add modified date every time
        properties.add(Functions.createEntity(dmp, getLocation(location), getClassType() + ":modified",
                ModelConstants.DATE_TIME_FORMATTER_ISO_8601.format(getModified())));
        return properties;
    }

    @Override
    public void build(EntityService entityService, String location) {
        // Set properties
        final List<Entity> properties = entityService.findEntities(location, null);

        Entity p = Functions.findPropertyInList(getClassType(), "created", properties);
        try {
            setCreated(p != null ? ModelConstants.DATE_TIME_FORMATTER_ISO_8601.parse(p.getValue()) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        p = Functions.findPropertyInList(getClassType(), "description", properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getClassType(), "ethical_issues_description", properties);
        setEthical_issues_description(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getClassType(), "ethical_issues_exist", properties);
        setEthical_issues_exist(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getClassType(), "ethical_issues_report", properties);
        setEthical_issues_report(p != null ? URI.create(p.getValue()) : null);

        p = Functions.findPropertyInList(getClassType(), "language", properties);
        setLanguage(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getClassType(), "modified", properties);
        try {
            setModified(p != null ? ModelConstants.DATE_TIME_FORMATTER_ISO_8601.parse(p.getValue()) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        p = Functions.findPropertyInList(getClassType(), "title", properties);
        setTitle(p != null ? p.getValue() : null);

        // Set identifier
        final Entity identifier = Functions.findPropertyInList(getClassType(), "identifier", properties);
        final Entity type = Functions.findPropertyInList(getClassType(), "type", properties);
        dmp_id = new DMP_id(identifier.getValue());
        if(type != null) {
            dmp_id.setType(type.getValue());
        }

        // Nested classes
        // Contact
        for (Entity property : entityService.findAllEntities(location, "contact:identifier")) {
            contact = new Contact();
            contact.build(entityService, location + "/" + property.getValue());
        }

        // Contributor
        for (Entity property : entityService.findAllEntities(location, "contributor:identifier")) {
            final Contributor i = new Contributor();
            i.build(entityService, location + "/" + property.getValue());
            contributor.add(i);
        }

        // Cost
        for (Entity property : entityService.findAllEntities(location, "cost:title")) {
            final Cost i = new Cost();
            i.build(entityService, location + "/" + property.getValue());
            cost.add(i);
        }

        // Project
        for (Entity property : entityService.findAllEntities(location, "project:title")) {
            final Project i = new Project();
            i.build(entityService, location + "/" + property.getValue());
            project.add(i);
        }

        // Dataset
        for (Entity property : entityService.findAllEntities(location, "dataset:identifier")) {
            final Dataset i = new Dataset();
            i.build(entityService, location + "/" + property.getValue());
            dataset.add(i);
        }

    }
}
