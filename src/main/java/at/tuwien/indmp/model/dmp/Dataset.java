package at.tuwien.indmp.model.dmp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.modul.PropertyModule;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.util.ModelConstants;
import at.tuwien.indmp.util.Functions;

public class Dataset extends AbstractClassObject {

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
    public String[] getPropertyNames() {
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
    public String getObjectIdentifier() {
        return getDataset_id().getObjectIdentifier();
    }

    @Override
    public List<Property> getPropertiesFromIdentifier(DMP dmp, String atLocation, RDMService rdmService) {
        return getDataset_id().getProperties(dmp, atLocation, rdmService);
    }

    @Override
    public List<Property> getPropertiesFromNestedObjects(DMP dmp, String atLocation, RDMService rdmService) {
        final List<Property> properties = new ArrayList<>();

        // ------------------------------------
        // Nested object: Distribution
        // ------------------------------------
        for (Distribution i : getDistribution()) {
            properties.addAll(i.getProperties(dmp, getAtLocation(atLocation), rdmService));
            properties.addAll(i.getPropertiesFromNestedObjects(dmp, getAtLocation(atLocation), rdmService));
        }
        
        // ------------------------------------
        // Nested object: Metadata
        // ------------------------------------
        for (Metadata i : getMetadata()) {
            properties.addAll(i.getProperties(dmp, getAtLocation(atLocation), rdmService));
        }

        // ------------------------------------
        // Nested object: Security and privacy
        // ------------------------------------
        for (SecurityAndPrivacy i : getSecurity_and_privacy()) {
            properties.addAll(i.getProperties(dmp, getAtLocation(atLocation), rdmService));
        }

        // ------------------------------------
        // Nested object: Technical resource
        // ------------------------------------
        for (TechnicalResource i : getTechnical_resource()) {
            properties.addAll(i.getProperties(dmp, getAtLocation(atLocation), rdmService));
        }

        return properties;
    }

    @Override
    public void build(PropertyModule propertyModule, String atLocation) {
        // ------------------------------------
        // Set properties
        // ------------------------------------
        final List<Property> properties = propertyModule.findEntities(atLocation, null, null, true);

        Property p = Functions.findPropertyInList(getSpecializationOf("data_quality_assurance"), properties);
        setData_quality_assurance(p != null
                ? Arrays.asList(p.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",", -1))
                : null);

        p = Functions.findPropertyInList(getSpecializationOf("description"), properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getSpecializationOf("issued"), properties);
        setIssued(p != null ? LocalDate.parse(p.getValue()) : null);

        p = Functions.findPropertyInList(getSpecializationOf("keyword"), properties);
        setKeyword(p != null
                ? Arrays.asList(p.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",", -1))
                : null);

        p = Functions.findPropertyInList(getSpecializationOf("language"), properties);
        setLanguage(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getSpecializationOf("personal_data"), properties);
        setPersonal_data(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getSpecializationOf("preservation_statement"), properties);
        setPreservation_statement(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getSpecializationOf("sensitive_data"), properties);
        setSensitive_data(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getSpecializationOf("title"), properties);
        setTitle(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getSpecializationOf("dtype"), properties);
        setType(p != null ? p.getValue() : null);

        // ------------------------------------
        // Set identifier
        // ------------------------------------
        final Property identifier = Functions.findPropertyInList(getSpecializationOf("identifier"), properties);
        final Property type = Functions.findPropertyInList(getSpecializationOf("type"), properties);
        dataset_id = new Dataset_id(identifier.getValue(), type.getValue());

        // ------------------------------------
        // Nested object: Set distribution
        // ------------------------------------
        for (Property property : propertyModule.findAllEntities(atLocation, "distribution:access_url", true)) {
            final Distribution i = new Distribution();
            i.build(propertyModule, atLocation + "/" + property.getValue());
            distribution.add(i);
        }

        // ------------------------------------
        // Nested object: Metadata
        // ------------------------------------
        for (Property property : propertyModule.findAllEntities(atLocation, "metadata:metadata_standard_id", true)) {
            final Metadata i = new Metadata();
            i.build(propertyModule, atLocation + "/" + property.getValue());
            metadata.add(i);
        }

        // ------------------------------------
        // Nested object: Security and privacy
        // ------------------------------------
        for (Property property : propertyModule.findAllEntities(atLocation, "securityandprivacy:title", true)) {
            final SecurityAndPrivacy i = new SecurityAndPrivacy();
            i.build(propertyModule, atLocation + "/" + property.getValue());
            security_and_privacy.add(i);
        }

        // ------------------------------------
        // Nested object: Technical resource
        // ------------------------------------
        for (Property property : propertyModule.findAllEntities(atLocation, "techicalresource:name", true)) {
            final TechnicalResource i = new TechnicalResource();
            i.build(propertyModule, atLocation + "/" + property.getValue());
            technical_resource.add(i);
        }

    }
}
