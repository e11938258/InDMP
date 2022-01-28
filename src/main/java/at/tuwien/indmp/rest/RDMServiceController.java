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
import org.springframework.web.bind.annotation.PathVariable;
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
     * @param rdmService
     */
    @RequestMapping(value = Endpoints.CREATE_NEW_RDM_SERVICE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void createNewRDMService(final @Valid @RequestBody RDMService rdmService) {
        rdmServiceLayer.create(rdmService);
    }

    /**
     * 
     * Update the service by id
     * 
     * @param rdmService
     */
    @RequestMapping(value = Endpoints.UPDATE_RDM_SERVICE, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateRDMService(final @Valid @RequestBody RDMService rdmService) {
        rdmServiceLayer.update(rdmService);
    }

    /**
     * Delete the service by id
     * 
     * @param id
     */
    @RequestMapping(value = Endpoints.DELETE_RDM_SERVICE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteRDMService(final @PathVariable("id") Long id) {
        rdmServiceLayer.delete(id);
    }

    /**
     *
     * Get all services
     *
     */
    @JsonView(Views.RDMService.class)
    @RequestMapping(value = Endpoints.GET_ALL_RDM_SERVICES, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RDMService> getAllRDMServices() {
        return rdmServiceLayer.getAllRDMServices();
    }
}
