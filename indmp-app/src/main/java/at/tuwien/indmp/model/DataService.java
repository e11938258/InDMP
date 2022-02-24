package at.tuwien.indmp.model;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.tuwien.indmp.util.ModelConstants;

/**
 * 
 * https://www.w3.org/TR/vocab-dcat-3/#Class:Data_Service
 * 
 */
@javax.persistence.Entity
@Table(name = "data_service")
public class DataService implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, precision = 18, scale = 0)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long identifier; // https://www.w3.org/TR/vocab-dcat-3/#Property:resource_identifier

    @Column(nullable = false)
    @NotNull
    @Size(min = ModelConstants.SOFTWARE_AGENT_TITLE_MIN, max = ModelConstants.SOFTWARE_AGENT_TITLE_MAX)
    @Pattern(regexp = ModelConstants.SOFTWARE_AGENT_TITLE_REGEX)
    private String title; // https://www.w3.org/TR/vocab-dcat-3/#Property:resource_title

    @Column(name = "access_rights", nullable = false, unique = true)
    @NotNull
    @Size(min = ModelConstants.SOFTWARE_AGENT_ACCESS_RIGHTS_MIN, max = ModelConstants.RDM_SERVICE_ACESS_RIGHTS_MAX)
    @Pattern(regexp = ModelConstants.RDM_SERVICE_ACCESS_RIGHTS_REGEX)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String accessRights; // https://www.w3.org/TR/vocab-dcat-3/#Property:resource_access_rights

    @Column(name = "endpoint_url", nullable = false, unique = true)
    @NotNull
    private URI endpointURL; // https://www.w3.org/TR/vocab-dcat-3/#Property:data_service_endpoint_url

    @Column(name = "endpoint_description")
    @Size(min = ModelConstants.SOFTWARE_AGENT_ENDPOINT_DESCRIPTION_MIN, max = ModelConstants.SOFTWARE_AGENT_ENDPOINT_DESCRIPTION_MAX)
    @Pattern(regexp = ModelConstants.SOFTWARE_AGENT_ENDPOINT_DESCRIPTION_REGEX)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String endpointDescription; // https://www.w3.org/TR/vocab-dcat-3/#Property:data_service_endpoint_description

    @ElementCollection
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private final List<String> rights = new ArrayList<>(); // https://www.w3.org/TR/vocab-dcat-3/#Property:resource_rights

    @OneToMany(mappedBy = "wasAssociatedWith", fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private final List<Activity> relation = new ArrayList<>(); // https://www.w3.org/TR/vocab-dcat-3/#Property:resource_relation

    public DataService() {
    }

    public Long getIdentifier() {
        return this.identifier;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getAccessRights() {
        return this.accessRights;
    }

    public void setAccessRights(String accessRights) {
        this.accessRights = accessRights;
    }

    public URI getEndpointURL() {
        return this.endpointURL;
    }

    public void setEndpointURL(URI endpointURL) {
        this.endpointURL = endpointURL;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getEndpointDescription() {
        return this.endpointDescription;
    }

    public void setEndpointDescription(String endpointDescription) {
        this.endpointDescription = endpointDescription;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<String> getRights() {
        return this.rights;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public List<Activity> getRelation() {
        return this.relation;
    }

    public void add(Activity activity) {
        relation.add(activity);
    }
}
