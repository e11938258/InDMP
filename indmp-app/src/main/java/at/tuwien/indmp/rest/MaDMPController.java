package at.tuwien.indmp.rest;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import at.tuwien.indmp.exception.NotFoundException;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.model.dmp.DMP;
import at.tuwien.indmp.model.dmp.DMPScheme;
import at.tuwien.indmp.model.idmp.IDMPScheme;
import at.tuwien.indmp.service.DMPService;
import at.tuwien.indmp.service.RDMServiceLayer;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MaDMPController {

    @Autowired
    private DMPService DMPService;

    @Autowired
    private RDMServiceLayer rdmServiceLayer;

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
    public void updateMaDMP(final Principal principal, final @Valid @RequestBody DMPScheme dmpScheme) {

        // Get current RDM Service
        final RDMService rdmService = rdmServiceLayer.findByClientId(principal.getName());

        // Identify maDMP
        DMP currentDMP = DMPService.identifyDMP(dmpScheme.getDmp(), rdmService);

        // Was the DMP found?
        if (currentDMP != null) {
            log.info("DMP was found, identifier: " + currentDMP.getClassIdentifier() + ". Updating...");
            // Update DMP
            DMPService.update(currentDMP, dmpScheme.getDmp(), rdmService);
        } else {
            // Create a new DMP
            DMPService.create(dmpScheme.getDmp(), rdmService);
            // Set current dmp
            currentDMP = dmpScheme.getDmp();
        }

        // Send DMP to all other services
        sendDMPToServices(currentDMP, rdmService);
    }

    /**
     *
     * Change identifier
     *
     * @param idmpScheme
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.IDENTIFIER_CHANGE, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void changeIdentifier(final Principal principal, final @Valid @RequestBody IDMPScheme idmpScheme) {

        // Get current RDM Service
        final RDMService rdmService = rdmServiceLayer.findByClientId(principal.getName());

        // Identify maDMP
        final DMP currentDMP = DMPService.identifyDMP(new DMP(idmpScheme.getIdmp().getCreated(),
                idmpScheme.getIdmp().getModified(), idmpScheme.getIdmp().getDmp_id()), rdmService);

        // Was the DMP found?
        if (currentDMP == null) {
            throw new NotFoundException(
                    "DMP not found, identifier: " + idmpScheme.getIdmp().getIdentifier().toString());
        }

        // Update modified date
        DMPService.updateModified(currentDMP, idmpScheme.getIdmp().getModified(), rdmService);

        // Change identifiers
        DMPService.changeIdentifiers(currentDMP, idmpScheme.getIdmp().getIdentifier(),
                idmpScheme.getIdmp().getModified(), rdmService);

        // Send DMP to all other services
        sendDMPToServices(currentDMP, rdmService);
    }

    /**
     *
     * Delete instance
     *
     * @param idmpScheme
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.DELETE_INSTANCE, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteInstance(final Principal principal, final @Valid @RequestBody IDMPScheme idmpScheme) {

        // Get current RDM Service
        final RDMService rdmService = rdmServiceLayer.findByClientId(principal.getName());

        // Identify maDMP
        final DMP currentDMP = DMPService.identifyDMP(new DMP(idmpScheme.getIdmp().getCreated(),
                idmpScheme.getIdmp().getModified(), idmpScheme.getIdmp().getDmp_id()), rdmService);

        // Was the DMP found?
        if (currentDMP == null) {
            throw new NotFoundException(
                    "DMP not found, identifier: " + idmpScheme.getIdmp().getIdentifier().toString());
        }

        // Update modified date
        DMPService.updateModified(currentDMP, idmpScheme.getIdmp().getModified(), rdmService);

        // Delete instances
        DMPService.deleteInstances(currentDMP, idmpScheme.getIdmp().getIdentifier());

        // Send DMP to all other services
        sendDMPToServices(currentDMP, rdmService);
    }

    @Async
    private void sendDMPToServices(DMP dmp, RDMService currentRdmService) {
        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Load whole dmp
        final DMPScheme dmpScheme = DMPService.loadWholeDMP(dmp);

        // For each service
        for (RDMService rdmService : rdmServiceLayer.getAllRDMServices()) {
            // Except from which information came
            if (!rdmService.equals(currentRdmService)) {
                final RestTemplate restTemplate = new RestTemplate();
                final HttpEntity<DMPScheme> request = new HttpEntity<>(dmpScheme, headers);
                log.info("Sending a new version of maDMP to " + rdmService.getDMPEndpoint());
                restTemplate.exchange(rdmService.getDMPEndpoint(), HttpMethod.PUT, request, Void.class);
            }
        }
    }

    /**
     *
     * Get history of identifiers by minimal DMP
     * 
     * @param dmpScheme
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.GET_MADMP_IDENTIFIERS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Property> getIdentifierHistory(final Principal principal, final @Valid @RequestBody DMPScheme dmpScheme) {

        // Check RDM Service
        rdmServiceLayer.findByClientId(principal.getName());

        // Identify maDMP
        final DMP dmpMinimum = DMPService.identifyDMP(dmpScheme.getDmp(), null);

        // Was the DMP found?
        if (dmpMinimum != null) {
            return DMPService.loadDMPIdentifiers(dmpMinimum);
        } else {
            throw new NotFoundException(
                    "DMP not found, identifier: " + dmpScheme.getDmp().getClassIdentifier());
        }
    }
}
