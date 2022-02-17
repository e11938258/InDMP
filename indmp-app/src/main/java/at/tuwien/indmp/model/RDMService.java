package at.tuwien.indmp.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import at.tuwien.indmp.util.ModelConstants;
import at.tuwien.indmp.util.Views;

@Entity
@Table(name = "rdm_service")
public class RDMService extends AbstractEntity {

    /* Properties */
    @Column(nullable = false)
    @NotNull
    @Size(min = ModelConstants.RDM_SERVICE_NAME_MIN, max = ModelConstants.RDM_SERVICE_NAME_MAX)
    @Pattern(regexp = ModelConstants.RDM_SERVICE_NAME_REGEX)
    @JsonView(Views.RDMService.class)
    private String name;

    @Column(name = "client_id", nullable = false)
    @NotNull
    @Size(min = ModelConstants.RDM_SERVICE_CLIENT_ID_MIN, max = ModelConstants.RDM_SERVICE_CLIENT_ID_MAX)
    @Pattern(regexp = ModelConstants.RDM_SERVICE_CLIENT_ID_REGEX)
    @JsonView(Views.RDMService.class)
    @JsonProperty("client_id")
    private String clientId;

    @Column(name = "dmp_endpoint", nullable = false)
    @NotNull
    @JsonView(Views.RDMService.class)
    @JsonProperty("dmp_endpoint")
    private URI DMPEndpoint;

    /* Nested data structure */
    @OneToMany(mappedBy = "rdmService", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<Property> properties = new ArrayList<>();

    @OneToMany(mappedBy = "rdmService", fetch = FetchType.EAGER, cascade = { CascadeType.MERGE,
            CascadeType.REMOVE })
    @JsonView(Views.RDMService.class)
    private final List<Permission> permissions = new ArrayList<>();

    public RDMService() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public URI getDMPEndpoint() {
        return this.DMPEndpoint;
    }

    public void setDMPEndpoint(URI DMPEndpoint) {
        this.DMPEndpoint = DMPEndpoint;
    }

    public List<Property> getProperties() {
        return this.properties;
    }

    public List<Permission> getPermissions() {
        return this.permissions;
    }

    public void add(final Property item) {
        properties.add(item);
    }

    public void add(final Permission item) {
        permissions.add(item);
    }

    public void remove(final Property item) {
        properties.removeIf(i -> Objects.equals(i.getId(), item.getId()));
    }

    public void remove(final Permission item) {
        permissions.removeIf(i -> Objects.equals(i.getId(), item.getId()));
    }

}
