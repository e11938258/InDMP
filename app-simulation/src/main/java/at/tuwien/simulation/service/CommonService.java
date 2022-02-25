package at.tuwien.simulation.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;

import at.tuwien.simulation.model.DataService;
import at.tuwien.simulation.util.Endpoints;
import at.tuwien.simulation.util.Functions;

@Service
public class CommonService {

    @Value("${application.name}")
    private String applicationName;

    @Value("${server.address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

    @Value("${indmp.host}")
    private String indmpHost;

    @Value("${indmp.endpoint.post.service}")
    private String indmpPostService;

    @Autowired
    private Environment environment;

    private final Logger log = LoggerFactory.getLogger(CommonService.class);

    /**
     * 
     * Register service to indmp
     * 
     * @throws URISyntaxException
     * 
     */
    public String registerService(OAuth2AuthorizedClient authorizedClient) throws URISyntaxException {

        // Set request with body
        final DataService dataService = new DataService(applicationName, authorizedClient.getPrincipalName(),
                new URI("http://" + serverAddress + ":" + serverPort + Endpoints.MADMP), "");
        setPermissions(dataService);
        final HttpEntity<DataService> request = new HttpEntity<>(dataService, Functions.getHeaders(authorizedClient));

        // Register a new service
        try {
            return Functions.processSuccess(log,
                    Functions.sendHTTPRequest(indmpHost + indmpPostService, HttpMethod.POST, request, String.class)
                            .getStatusCode().toString());
        } catch (Throwable e) {
            return Functions.processError(log, e.getMessage());
        }
    }

    private void setPermissions(DataService dataService) {
        if (environment.getActiveProfiles().length > 0 && environment.getActiveProfiles()[0].equals("repository")) {
            dataService.add("contact");
            dataService.add("contributor");
            dataService.add("dataset");
            dataService.add("distribution");
            dataService.add("host");
            dataService.add("license");
            dataService.add("metadata");
            dataService.add("securityandprivacy");
        } else {
            dataService.add("contact");
            dataService.add("contributor");
            dataService.add("cost");
            dataService.add("dataset");
            dataService.add("distribution");
            dataService.add("dmp");
            dataService.add("funding");
            dataService.add("grant_id");
            dataService.add("host");
            dataService.add("license");
            dataService.add("metadata");
            dataService.add("project");
            dataService.add("securityandprivacy");
            dataService.add("technicalresource");
        }
    }

}
