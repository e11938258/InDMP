package at.tuwien.indmp.model.dmp;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.System;
import at.tuwien.indmp.service.PropertyService;
import at.tuwien.indmp.util.Functions;
import at.tuwien.indmp.util.dmp.AllowedValues;
import at.tuwien.indmp.util.dmp.DataIdentifierType;
import at.tuwien.indmp.util.dmp.Language;
import at.tuwien.indmp.util.var.ServiceType;

public class DMP extends ClassEntity {

    public final static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

    /* Properties */
    @NotNull
    @JsonFormat(pattern = DATE_FORMAT)
    private Date created;

    private String description;

    private String ethical_issues_description;

    private AllowedValues ethical_issues_exist;

    private URI ethical_issues_report;

    private Language language;

    @NotNull
    @JsonFormat(pattern = DATE_FORMAT)
    private Date modified;

    private String title;

    /* Nested data structure */
    private Contact contact;

    private List<Contributor> contributor = new ArrayList<>();

    private List<Cost> cost = new ArrayList<>();

    private List<Dataset> dataset = new ArrayList<>();

    @NotNull
    private Dmp_id dmp_id;

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
    public DMP(Date created, Date modified, Dmp_id dmp_id) {
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
        return DATE_FORMATTER.format(this.created);
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

    public AllowedValues getEthical_issues_exist() {
        return this.ethical_issues_exist;
    }

    public void setEthical_issues_exist(AllowedValues ethical_issues_exist) {
        this.ethical_issues_exist = ethical_issues_exist;
    }

    public URI getEthical_issues_report() {
        return this.ethical_issues_report;
    }

    public void setEthical_issues_report(URI ethical_issues_report) {
        this.ethical_issues_report = ethical_issues_report;
    }

    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language language) {
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

    public Dmp_id getDmp_id() {
        return this.dmp_id;
    }

    public void setDmp_id(Dmp_id dmp_id) {
        this.dmp_id = dmp_id;
    }

    public List<Project> getProject() {
        return this.project;
    }

    public void setProject(List<Project> project) {
        this.project = project;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                DATE_FORMATTER.format(getCreated()),
                getDescription(),
                getEthical_issues_description(),
                getEthical_issues_exist(),
                getEthical_issues_report(),
                getLanguage(),
                DATE_FORMATTER.format(getModified()),
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
                "modified",
                "title",
        };
    }

    @Override
    public String getClassIdentifier() {
        return getDmp_id().getClassIdentifier();
    }

    @Override
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String reference, System system) {
        return dmp_id.getProperties(dmp, reference, system);
    }

    @Override
    public List<Property> getPropertiesFromNestedClasses(DMP dmp, System system) {
        final List<Property> properties = new ArrayList<>();

        // Contact
        if (getContact() != null) {
            properties.addAll(getContact().getProperties(dmp, getClassIdentifier(), system));
        }
        // Contributor
        for (Contributor i : getContributor()) {
            properties.addAll(i.getProperties(dmp, getClassIdentifier(), system));
        }
        // Cost
        for (Cost i : getCost()) {
            properties.addAll(i.getProperties(dmp, getClassIdentifier(), system));
        }
        // Dataset
        for (Dataset i : getDataset()) {
            properties.addAll(i.getProperties(dmp, getClassIdentifier(), system));
            properties.addAll(i.getPropertiesFromNestedClasses(dmp, system));
        }
        // Project
        for (Project i : getProject()) {
            properties.addAll(i.getProperties(dmp, getClassIdentifier(), system));
            properties.addAll(i.getPropertiesFromNestedClasses(dmp, system));
        }

        return properties;
    }

    @Override
    public boolean hasRightsToUpdate(System system) {
        return Functions.isServiceTypeInArray(new ServiceType[] {
                ServiceType.DMP_APP,
                ServiceType.ADMINISTRATIVE_DATA_COLLECTOR,
        }, system.getType());
    }

    @Override
    public void build(PropertyService propertyService, String dmpIdentifier, String classIdentifier) {
        // Nested classes
        // Contact
        final Property contactIdentifier = propertyService.findProperty(dmpIdentifier, "contact_id", null, "identifier", null, null);
        if(contactIdentifier != null) {
            contact = new Contact();
            contact.build(propertyService, dmpIdentifier, contactIdentifier.getValue());
        }

        // Contributors
        for(Property property: propertyService.findProperties(dmpIdentifier, "contributor_id", null, "identifier", null, null)) {
            final Contributor i = new Contributor();
            i.build(propertyService, dmpIdentifier, property.getValue());
            contributor.add(i);
        }

        // Cost
        for(Property property: propertyService.findProperties(dmpIdentifier, "cost", null, "title", null, null)) {
            final Cost i = new Cost();
            i.build(propertyService, dmpIdentifier, property.getValue());
            cost.add(i);
        }

        // Project
        for(Property property: propertyService.findProperties(dmpIdentifier, "project", null, "title", null, null)) {
            final Project i = new Project();
            i.build(propertyService, dmpIdentifier, property.getValue());
            project.add(i);
        }

        // Dataset
        for(Property property: propertyService.findProperties(dmpIdentifier, "dataset_id", null, "identifier", null, null)) {
            final Dataset i = new Dataset();
            i.build(propertyService, dmpIdentifier, property.getValue());
            dataset.add(i);
        }

        // Set properties
        final List<Property> properties = propertyService.findProperties(dmpIdentifier, "dmp", null, null,
                null, null);

        Property p = Functions.findPropertyInList("created", properties);
        try {
            setCreated(p != null ? DATE_FORMATTER.parse(p.getValue()) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        p = Functions.findPropertyInList("description", properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("ethical_issues_description", properties);
        setEthical_issues_description(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("ethical_issues_exist", properties);
        setEthical_issues_exist(p != null ? AllowedValues.valueOf(p.getValue()) : null);

        p = Functions.findPropertyInList("ethical_issues_report", properties);
        setEthical_issues_report(p != null ? URI.create(p.getValue()) : null);

        p = Functions.findPropertyInList("language", properties);
        setLanguage(p != null ? Language.valueOf(p.getValue()) : null);

        p = Functions.findPropertyInList("modified", properties);
        try {
            setModified(p != null ? DATE_FORMATTER.parse(p.getValue()) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        p = Functions.findPropertyInList("title", properties);
        setTitle(p != null ? p.getValue() : null);

        // Set identifier
        final List<Property> identifierProperties = propertyService.findProperties(dmpIdentifier, "dmp_id", null,
                null, null, null);
        final Property identifier = Functions.findPropertyInList("identifier", identifierProperties);
        final Property type = Functions.findPropertyInList("type", identifierProperties);
        dmp_id = new Dmp_id(identifier.getValue(), DataIdentifierType.valueOf(type.getValue()));

    }
}
