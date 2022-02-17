package at.tuwien.dmp.rest;

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

import at.tuwien.dmp.model.Permission;
import at.tuwien.dmp.model.RDMService;
import at.tuwien.dmp.util.Endpoints;
import at.tuwien.dmp.util.Functions;

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
        final RDMService rdmService = new RDMService(applicationName, authorizedClient.getPrincipalName(),
                new URI("http://" + serverAddress + ":" + serverPort + Endpoints.MADMP));
        setPermissions(rdmService);
        final HttpEntity<RDMService> request = new HttpEntity<>(rdmService, Functions.getHeaders(authorizedClient));

        // Register a new service
        try {
            return Functions.processResponse(log,
                    Functions.sendHTTPRequest(indmpHost + indmpPostService, HttpMethod.POST, request));
        } catch (HttpClientErrorException e) {
            return Functions.processError(log, e);
        }
    }

    private void setPermissions(RDMService rdmService) {
        rdmService.add(new Permission("contact", true));
        rdmService.add(new Permission("contributor", true));
        rdmService.add(new Permission("cost", true));
        rdmService.add(new Permission("dataset", true));
        rdmService.add(new Permission("distribution", true));
        rdmService.add(new Permission("dmp", true));
        rdmService.add(new Permission("funding", true));
        rdmService.add(new Permission("grant_id", true));
        rdmService.add(new Permission("host", true));
        rdmService.add(new Permission("license", true));
        rdmService.add(new Permission("metadata", true));
        rdmService.add(new Permission("project", true));
        rdmService.add(new Permission("securityandprivacy", true));
        rdmService.add(new Permission("technicalresource", true));
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
