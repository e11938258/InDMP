package at.tuwien.simulation.rest;

import java.net.URISyntaxException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import at.tuwien.simulation.model.dmp.DMPScheme;
import at.tuwien.simulation.service.CommonService;
import at.tuwien.simulation.util.Endpoints;

@RestController
public class CommonController {

    @Autowired
    private CommonService commonService;

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
    public String init(@RegisteredOAuth2AuthorizedClient("indmp-client") OAuth2AuthorizedClient authorizedClient)
            throws URISyntaxException {
        return commonService.registerService(authorizedClient);
    }

    /**
     *
     * Update maDMP
     *
     * @param dmp
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.MADMP, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateMaDMP(final @Valid @RequestBody DMPScheme dmpScheme) {
        log.info("Receive a new madmp: " + dmpScheme.toString());
    }
}
