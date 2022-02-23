package at.tuwien.repository.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import at.tuwien.repository.model.DataService;
import at.tuwien.repository.util.Endpoints;
import at.tuwien.repository.util.Functions;

@RestController
public class CommonController {

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

    private final Logger log = LoggerFactory.getLogger(CommonController.class);

    /**
     * 
     * Register service to indmp
     * 
     * @throws URISyntaxException
     * 
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.INIT, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String init(
            @RegisteredOAuth2AuthorizedClient("indmp-client") OAuth2AuthorizedClient authorizedClient)
            throws URISyntaxException {

        // Set request with body
        final DataService dataService = new DataService(applicationName, authorizedClient.getPrincipalName(),
                new URI("http://" + serverAddress + ":" + serverPort + Endpoints.MADMP), "");
        setPermissions(dataService);
        final HttpEntity<DataService> request = new HttpEntity<>(dataService, Functions.getHeaders(authorizedClient));

        // Register a new service
        try {
            return Functions.processResponse(log,
                    Functions.sendHTTPRequest(indmpHost + indmpPostService, HttpMethod.POST, request));
        } catch (HttpClientErrorException e) {
            return Functions.processError(log, e);
        }
    }

    private void setPermissions(DataService dataService) {
        dataService.add("contact");
        dataService.add("contributor");
        dataService.add("dataset");
        dataService.add("distribution");
        dataService.add("host");
        dataService.add("license");
        dataService.add("metadata");
        dataService.add("securityandprivacy");
    }

    /**
     *
     * Update maDMP
     *
     * @param dmp
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.MADMP, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateMaDMP(final @Valid @RequestBody String dmpScheme) {
        log.info("Receive a new madmp: " + dmpScheme);
    }
}
