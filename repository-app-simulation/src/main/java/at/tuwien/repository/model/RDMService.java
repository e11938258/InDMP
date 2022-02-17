package at.tuwien.repository.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RDMService {

    /* Properties */
    @NotNull
    private String name;

    @NotNull
    @JsonProperty("client_id")
    private String clientId;

    @NotNull
    @JsonProperty("dmp_endpoint")
    private URI DMPEndpoint;

    /* Nested data structure */
    private final List<Permission> permissions = new ArrayList<>();

    public RDMService() {
    }

    public RDMService(String name, String clientId, URI DMPEndpoint) {
        this.name = name;
        this.clientId = clientId;
        this.DMPEndpoint = DMPEndpoint;
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

    public List<Permission> getPermissions() {
        return this.permissions;
    }

    public void add(final Permission item) {
        permissions.add(item);
    }
}
