package at.tuwien.dmp.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import at.tuwien.dmp.util.ModelConstants;

public class DataService {

    @Size(min = ModelConstants.SOFTWARE_AGENT_TITLE_MIN, max = ModelConstants.SOFTWARE_AGENT_TITLE_MAX)
    @Pattern(regexp = ModelConstants.SOFTWARE_AGENT_TITLE_REGEX)
    private String title;

    @Size(min = ModelConstants.SOFTWARE_AGENT_ACCESS_RIGHTS_MIN, max = ModelConstants.RDM_SERVICE_ACESS_RIGHTS_MAX)
    @Pattern(regexp = ModelConstants.RDM_SERVICE_ACCESS_RIGHTS_REGEX)
    private String accessRights;

    @NotNull
    private URI endpointURL;

    @NotNull
    @Size(min = ModelConstants.SOFTWARE_AGENT_ENDPOINT_DESCRIPTION_MIN, max = ModelConstants.SOFTWARE_AGENT_ENDPOINT_DESCRIPTION_MAX)
    @Pattern(regexp = ModelConstants.SOFTWARE_AGENT_ENDPOINT_DESCRIPTION_REGEX)
    private String endpointDescription;

    private final List<String> rights = new ArrayList<>();

    public DataService(String title, String accessRights, URI endpointURL, String endpointDescription) {
        this.title = title;
        this.accessRights = accessRights;
        this.endpointURL = endpointURL;
        this.endpointDescription = endpointDescription;
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

    public String getEndpointDescription() {
        return this.endpointDescription;
    }

    public void setEndpointDescription(String endpointDescription) {
        this.endpointDescription = endpointDescription;
    }

    public List<String> getRights() {
        return this.rights;
    }

    public void add(String right) {
        rights.add(right);
    }
}
