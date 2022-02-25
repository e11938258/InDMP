package at.tuwien.indmp.rest;

import java.security.Principal;

import javax.validation.Valid;

import at.tuwien.indmp.exception.NotFoundException;
import at.tuwien.indmp.model.DataService;
import at.tuwien.indmp.service.DataServiceService;
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
public class DataServiceController {

    @Autowired
    private DataServiceService dataServiceService;

    public DataServiceController() {
    }

    /**
     *
     * Create a new service
     *
     * @param dataService
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = Endpoints.CREATE_DATA_SERVICE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void createNewRDMService(@Valid @RequestBody DataService dataService) {
        dataServiceService.persist(dataService);
    }

    /**
     *
     * Exists service by client id - used only for testing
     *
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.EXISTS_DATA_SERVICE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean existsByClientId(Principal principal) {
        try {
            if (dataServiceService.findByAccessRights(principal.getName()) != null) {
                return true;
            } else {
                return false;
            }
        } catch (NotFoundException ex) {
            return false;
        }
    }
}
