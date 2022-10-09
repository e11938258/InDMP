package at.tuwien.indmp.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import at.tuwien.indmp.util.ModelConstants;
import at.tuwien.indmp.util.RDMServiceState;
import at.tuwien.indmp.util.Views;

/**
 * 
 * RDM service class
 * 
 * https://www.w3.org/TR/vocab-dcat-3/#Class:Data_Service
 * 
 */
@Entity
@Table(name = "rdm_service", uniqueConstraints = {
        @UniqueConstraint(columnNames = "access_rights")
})
public class RDMService extends AbstractEntity {

    @Column(nullable = false)
    @NotNull
    @Size(min = ModelConstants.RDM_SERVICE_TITLE_MIN, max = ModelConstants.RDM_SERVICE_TITLE_MAX)
    @Pattern(regexp = ModelConstants.RDM_SERVICE_TITLE_REGEX)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonView(Views.Basic.class)
    private String title; // https://www.w3.org/TR/vocab-dcat-3/#Property:resource_title

    @Column(name = "access_rights", nullable = false, unique = true)
    @NotNull
    @Size(min = ModelConstants.RDM_SERVICE_ACCESS_RIGHTS_MIN, max = ModelConstants.RDM_SERVICE_ACCESS_RIGHTS_MAX)
    @Pattern(regexp = ModelConstants.RDM_SERVICE_ACCESS_RIGHTS_REGEX)
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonView(Views.Extended.class)
    private String accessRights; // https://www.w3.org/TR/vocab-dcat-3/#Property:resource_access_rights

    @Column(name = "endpoint_url", nullable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @JsonView(Views.Extended.class)
    private URI endpointURL; // https://www.w3.org/TR/vocab-dcat-3/#Property:data_service_endpoint_url

    @Column(nullable = false)
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    @JsonView(Views.Extended.class)
    private RDMServiceState state = RDMServiceState.UNSYNCHRONIZED; 

    @ElementCollection
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(Views.Extended.class)
    private List<String> propertyRights = new ArrayList<>(); // https://www.w3.org/TR/vocab-dcat-3/#Property:resource_rights

    @OneToMany(mappedBy = "wasStartedBy", fetch = FetchType.LAZY)
    @JsonIgnore
    private final List<Activity> startedRelation = new ArrayList<>(); // https://www.w3.org/TR/vocab-dcat-3/#Property:resource_relation

    @OneToMany(mappedBy = "wasEndedBy", fetch = FetchType.LAZY)
    @JsonIgnore
    private final List<Activity> endedRelation = new ArrayList<>(); // https://www.w3.org/TR/vocab-dcat-3/#Property:resource_relation

    public RDMService() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public RDMServiceState getState() {
        return this.state;
    }

    public void setState(RDMServiceState state) {
        this.state = state;
    }

    public List<String> getPropertyRights() {
        return this.propertyRights;
    }

    public void setPropertyRights(List<String> propertyRights) {
        this.propertyRights = propertyRights;
    }
    
    @JsonIgnore
    public List<Activity> getStartedRelation() {
        return this.startedRelation;
    }

    public void addStartRelation(Activity activity) {
        startedRelation.add(activity);
    }

    @JsonIgnore
    public List<Activity> getEndedRelation() {
        return this.endedRelation;
    }

    public void addEndRelation(Activity activity) {
        endedRelation.add(activity);
    }

    /* Non-standard */

    /**
     * 
     * Has the RDM service right to the property
     * 
     * @param property
     * @return
     */
    public boolean hasPropertyRight(Property property) {
        return propertyRights.contains(property.getSpecializationOf());
    }
}
