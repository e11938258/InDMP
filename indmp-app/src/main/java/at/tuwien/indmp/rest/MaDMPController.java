package at.tuwien.indmp.rest;

import java.security.Principal;
import java.sql.Date;
import java.util.List;

import javax.validation.Valid;

import at.tuwien.indmp.exception.ForbiddenException;
import at.tuwien.indmp.exception.NotFoundException;
import at.tuwien.indmp.model.DataService;
import at.tuwien.indmp.model.Entity;
import at.tuwien.indmp.model.dmp.DMP;
import at.tuwien.indmp.model.dmp.DMPScheme;
import at.tuwien.indmp.model.dmp.DMP_id;
import at.tuwien.indmp.service.DMPService;
import at.tuwien.indmp.service.DataServiceService;
import at.tuwien.indmp.util.Endpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MaDMPController {

    @Autowired
    private DMPService DMPService;

    @Autowired
    private DataServiceService dataServiceService;

    private final Logger log = LoggerFactory.getLogger(MaDMPController.class);

    public MaDMPController() {
    }

    /**
     *
     * Update maDMP
     *
     * @param dmp
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.UPDATE_MADMP, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateMaDMP(Principal principal, @Valid @RequestBody DMPScheme dmpScheme) {

        // Get current RDM Service
        final DataService dataService = dataServiceService.findByClientId(principal.getName());

        // Identify maDMP
        DMP currentDMP = DMPService.identifyDMP(dmpScheme.getDmp(), dataService);

        // Was the DMP found?
        if (currentDMP != null) {
            log.info("DMP was found, identifier: " + currentDMP.getClassIdentifier() + ". Updating...");
            // Update DMP
            DMPService.update(dmpScheme.getDmp(), dataService);
        } else {
            // Create a new DMP
            DMPService.create(dmpScheme.getDmp(), dataService);
            // Set current dmp
            currentDMP = dmpScheme.getDmp();
        }

        // Send DMP to all other services
        sendDMPToServices(currentDMP, dataService);
    }

    /**
     *
     * Change identifier
     *
     * @param idmpScheme
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.UPDATE_MADMP_IDENTIFIER, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void changeIdentifier(final Principal principal,
            @RequestParam(required = true) String identifier,
            @RequestParam(required = true) Date created,
            @RequestParam(required = true) Date modified,
            @Valid @RequestBody Entity entity) {

        // Get current RDM Service
        final DataService dataService = dataServiceService.findByClientId(principal.getName());

        // Identify maDMP
        final DMP currentDMP = DMPService.identifyDMP(new DMP(created, modified, new DMP_id(identifier)), dataService);

        // Was the DMP found?
        if (currentDMP == null) {
            throw new NotFoundException("DMP not found, identifier: " + identifier);
        }

        // Change identifier
        DMPService.changeIdentifiers(currentDMP, entity, dataService);

        // Send DMP to all other services
        sendDMPToServices(currentDMP, dataService);
    }

    /**
     *
     * Delete instance
     *
     * @param idmpScheme
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.DELETE_MADMP_INSTANCE, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteInstance(Principal principal,
            @RequestParam(required = true) String identifier,
            @RequestParam(required = true) Date created,
            @RequestParam(required = true) Date modified,
            @Valid @RequestBody Entity entity) {

        // Get current RDM Service
        final DataService dataService = dataServiceService.findByClientId(principal.getName());

        // Identify maDMP
        final DMP currentDMP = DMPService.identifyDMP(new DMP(created, modified, new DMP_id(identifier)), dataService);

        // Checking location
        if (!entity.getAtLocation().startsWith(currentDMP.getLocation(""))) {
            throw new ForbiddenException("Cannot find location in dmp: " + entity.getAtLocation());
        }

        // Delete instance
        DMPService.deleteInstance(entity);

        // Send DMP to all other services
        sendDMPToServices(currentDMP, dataService);
    }

    @Async
    private void sendDMPToServices(DMP dmp, DataService dataService) {
        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Load whole dmp
        final DMPScheme dmpScheme = DMPService.loadWholeDMP(dmp);

        // For each service
        for (DataService service : dataServiceService.getAllDataServices()) {
            // Except from which information received
            if (!service.equals(dataService)) {
                final RestTemplate restTemplate = new RestTemplate();
                final HttpEntity<DMPScheme> request = new HttpEntity<>(dmpScheme, headers);
                log.info("Sending a new version of maDMP to " + service.getEndpointURL());
                restTemplate.exchange(service.getEndpointURL(), HttpMethod.PUT, request, Void.class);
            }
        }
    }

    /**
     *
     * Get history of identifiers
     * 
     * @param dmpScheme
     */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = Endpoints.GET_MADMP_IDENTIFIERS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Entity> getIdentifierHistory(Principal principal,
            @RequestParam(required = true) String identifier,
            @RequestParam(required = true) Date created,
            @RequestParam(required = true) Date modified) {

        // Get current RDM Service
        final DataService dataService = dataServiceService.findByClientId(principal.getName());

        // Identify maDMP
        final DMP currentDMP = DMPService.identifyDMP(new DMP(created, modified, new DMP_id(identifier)), dataService);

        // Was the DMP found?
        if (currentDMP != null) {
            return DMPService.loadIdentifierHistory(currentDMP);
        } else {
            throw new NotFoundException("DMP not found, identifier: " + identifier);
        }
    }

    /**
     *
     * Get maDMP by minimal DMP - just for test cases
     * 
     * @param dmpScheme
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.GET_MADMP, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DMPScheme getMaDMP(@Valid @RequestBody DMPScheme dmpScheme) {
        final DMP dmpMinimum = DMPService.identifyDMP(dmpScheme.getDmp(), null);
        if (dmpMinimum != null) {
            return DMPService.loadWholeDMP(dmpMinimum);
        } else {
            return null;
        }
    }
}
