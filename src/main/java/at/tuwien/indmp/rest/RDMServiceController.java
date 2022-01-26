package at.tuwien.indmp.rest;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonView;

import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.service.RDMServiceLayer;
import at.tuwien.indmp.util.Endpoints;
import at.tuwien.indmp.util.Views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
     * @param system
     */
    @RequestMapping(value = Endpoints.CREATE_NEW_RDM_SERVICE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void createNewRDMService(final @Valid @RequestBody RDMService system) {
        rdmServiceLayer.create(system);
    }

    /**
     *
     * Get all services
     *
     * @param system
     */
    @JsonView(Views.RDMService.class)
    @RequestMapping(value = Endpoints.GET_ALL_RDM_SERVICES, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RDMService> getAllRDMServices() {
        return rdmServiceLayer.getAllRDMServices();
    }
}
