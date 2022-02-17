package at.tuwien.indmp.rest;

import javax.validation.Valid;

import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.service.RDMServiceLayer;
import at.tuwien.indmp.util.Endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RDMServiceController {

    @Autowired
    private RDMServiceLayer rdmServiceLayer;

    public RDMServiceController() {
    }

    /**
     *
     * Create a new service
     *
     * @param rdmService
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = Endpoints.CREATE_NEW_RDM_SERVICE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void createNewRDMService(final @Valid @RequestBody RDMService rdmService) {
        rdmServiceLayer.create(rdmService);
    }
}
