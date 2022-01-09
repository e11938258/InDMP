package at.tuwien.indmp.model.dmp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.System;
import at.tuwien.indmp.service.PropertyService;
import at.tuwien.indmp.util.Functions;
import at.tuwien.indmp.util.dmp.AllowedValues;
import at.tuwien.indmp.util.dmp.DataIdentifierType;
import at.tuwien.indmp.util.dmp.Language;
import at.tuwien.indmp.util.var.ServiceType;

public class Dataset extends ClassEntity {

    public final static String DATE_FORMAT = "yyyy-MM-dd";
    public final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);

    /* Properties */
    private List<String> data_quality_assurance = new ArrayList<>();

    private String description;

    @JsonFormat(pattern = DATE_FORMAT)
    private Date issued;

    private List<String> keyword = new ArrayList<>();

    private Language language;

    @NotNull
    private AllowedValues personal_data;

    private String preservation_statement;

    @NotNull
    private AllowedValues sensitive_data;

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

    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public AllowedValues getPersonal_data() {
        return this.personal_data;
    }

    public void setPersonal_data(AllowedValues personal_data) {
        this.personal_data = personal_data;
    }

    public String getPreservation_statement() {
        return this.preservation_statement;
    }

    public void setPreservation_statement(String preservation_statement) {
        this.preservation_statement = preservation_statement;
    }

    public AllowedValues getSensitive_data() {
        return this.sensitive_data;
    }

    public void setSensitive_data(AllowedValues sensitive_data) {
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
                getData_quality_assurance().toString(),
                getDescription(),
                getIssued() != null ? DATE_FORMATTER.format(getIssued()) : null,
                getKeyword().toString(),
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
                "type",
        };
    }

    @Override
    public String getClassIdentifier() {
        return getDataset_id().getClassIdentifier();
    }

    @Override
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String reference, System system) {
        return getDataset_id().getProperties(dmp, reference, system);
    }

    @Override
    public List<Property> getPropertiesFromNestedClasses(DMP dmp, System system) {
        final List<Property> properties = new ArrayList<>();

        // Distribution
        for (Distribution i : getDistribution()) {
            properties.addAll(i.getProperties(dmp, getClassIdentifier(), system));
            properties.addAll(i.getPropertiesFromNestedClasses(dmp, system));
        }
        // Metadata
        for (Metadata i : getMetadata()) {
            properties.addAll(i.getProperties(dmp, getClassIdentifier(), system));
        }
        // Security and privacy
        for (SecurityAndPrivacy i : getSecurity_and_privacy()) {
            properties.addAll(i.getProperties(dmp, getClassIdentifier(), system));
        }
        // Technical resource
        for (TechnicalResource i : getTechnical_resource()) {
            properties.addAll(i.getProperties(dmp, getClassIdentifier(), system));
        }

        return properties;
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
        // Nested classes
        // Distribution
        for (Property property : propertyService.findProperties(dmpIdentifier, "distribution", null, "access_url", null,
                classIdentifier)) {
            final Distribution i = new Distribution();
            i.build(propertyService, dmpIdentifier, property.getValue());
            distribution.add(i);
        }

        // Metadata
        for (Property property : propertyService.findProperties(dmpIdentifier, "metadata_standard_id", null,
                "identifier", null, classIdentifier)) {
            final Metadata i = new Metadata();
            i.build(propertyService, dmpIdentifier, property.getValue());
            metadata.add(i);
        }

        // Security and privacy
        for (Property property : propertyService.findProperties(dmpIdentifier, "securityandprivacy", null, "title",
                null, classIdentifier)) {
            final SecurityAndPrivacy i = new SecurityAndPrivacy();
            i.build(propertyService, dmpIdentifier, property.getValue());
            security_and_privacy.add(i);
        }

        // Technical resource
        for (Property property : propertyService.findProperties(dmpIdentifier, "techicalresource", null, "name", null,
                classIdentifier)) {
            final TechnicalResource i = new TechnicalResource();
            i.build(propertyService, dmpIdentifier, property.getValue());
            technical_resource.add(i);
        }

        // Set properties
        final List<Property> properties = propertyService.findProperties(dmpIdentifier, "dataset", classIdentifier,
                null, null, null);

        Property p = Functions.findPropertyInList("data_quality_assurance", properties);
        setData_quality_assurance(p != null
                ? Arrays.asList(p.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",", -1))
                : null);

        p = Functions.findPropertyInList("description", properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("issued", properties);
        try {
            setIssued(p != null ? DATE_FORMATTER.parse(p.getValue()) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        p = Functions.findPropertyInList("keyword", properties);
        setKeyword(p != null
                ? Arrays.asList(p.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",", -1))
                : null);

        p = Functions.findPropertyInList("language", properties);
        setLanguage(p != null ? Language.valueOf(p.getValue()) : null);

        p = Functions.findPropertyInList("personal_data", properties);
        setPersonal_data(p != null ? AllowedValues.valueOf(p.getValue()) : null);

        p = Functions.findPropertyInList("preservation_statement", properties);
        setPreservation_statement(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("sensitive_data", properties);
        setSensitive_data(p != null ? AllowedValues.valueOf(p.getValue()) : null);

        p = Functions.findPropertyInList("title", properties);
        setTitle(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList("type", properties);
        setType(p != null ? p.getValue() : null);

        // Set identifier
        final List<Property> identifierProperties = propertyService.findProperties(dmpIdentifier, "dataset_id",
                classIdentifier, null, null, null);
        final Property identifier = Functions.findPropertyInList("identifier", identifierProperties);
        final Property type = Functions.findPropertyInList("type", identifierProperties);
        dataset_id = new Dataset_id(identifier.getValue(), DataIdentifierType.valueOf(type.getValue()));

    }
}
