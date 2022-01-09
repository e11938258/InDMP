package at.tuwien.indmp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import at.tuwien.indmp.util.Constants;
import at.tuwien.indmp.util.Views;
import at.tuwien.indmp.util.var.ServiceType;

@Entity
@Table(name = "system", indexes = {
        @Index(name = "idx_id", columnList = "id", unique = true) })
public class System extends AbstractEntity {

    /* Properties */
    @Column(nullable = false, length = Constants.SYSTEM_NAME_MAX)
    @NotNull
    @Size(min = Constants.SYSTEM_NAME_MIN, max = Constants.SYSTEM_NAME_MAX)
    @Pattern(regexp = Constants.SYSTEM_NAME_REGEX)
    @JsonView(Views.Basic.class)
    private String name;

    @Column(nullable = false, length = Constants.SYSTEM_HOST_MAX)
    @NotNull
    @Size(min = Constants.SYSTEM_HOST_MIN, max = Constants.SYSTEM_HOST_MAX)
    @Pattern(regexp = Constants.SYSTEM_HOST_REGEX)
    @JsonView(Views.Basic.class)
    private String host;

    @Column(nullable = false, length = Constants.SYSTEM_ENDPOINT_MAX, name = "dmp_endpoint")
    @NotNull
    @Size(min = Constants.SYSTEM_ENDPOINT_MIN, max = Constants.SYSTEM_ENDPOINT_MAX)
    @Pattern(regexp = Constants.SYSTEM_ENDPOINT_REGEX)
    @JsonProperty("dmp_endpoint")
    private String DMPEndpoint;

    @NotNull
    @Enumerated(EnumType.STRING)
    @JsonView(Views.Basic.class)
    private ServiceType type;

    /* Nested data structure */
    @JsonIgnore
    @OneToMany(mappedBy = "system", fetch = FetchType.LAZY)
    private final List<Property> properties = new ArrayList<>();

    public System() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @JsonIgnore
    public String getDMPEndpoint() {
        return this.DMPEndpoint;
    }

    public void setDMPEndpoint(String DMPEndpoint) {
        this.DMPEndpoint = DMPEndpoint;
    }

    @JsonIgnore
    public List<Property> getProperties() {
        return this.properties;
    }

    public ServiceType getType() {
        return this.type;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    @JsonIgnore
    public List<Property> getHistory() {
        return this.properties;
    }

    public void add(final Property item) {
        properties.add(item);
    }

    public void remove(final Property item) {
        properties.removeIf(i -> Objects.equals(i.getId(), item.getId()));
    }

}
