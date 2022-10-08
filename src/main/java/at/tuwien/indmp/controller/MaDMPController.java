package at.tuwien.indmp.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import at.tuwien.indmp.exception.BadRequestException;
import at.tuwien.indmp.exception.ForbiddenException;
import at.tuwien.indmp.exception.NotFoundException;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.model.dmp.DMP;
import at.tuwien.indmp.model.dmp.DMPScheme;
import at.tuwien.indmp.model.dmp.DMP_id;
import at.tuwien.indmp.modul.DMPModule;
import at.tuwien.indmp.modul.RDMServiceModule;
import at.tuwien.indmp.util.Endpoints;
import at.tuwien.indmp.util.RDMServiceState;

@RestController
public class MaDMPController {

    @Autowired
    private DMPModule dmpModule;

    @Autowired
    private RDMServiceModule rdmServiceModule;

    private final Logger log = LoggerFactory.getLogger(MaDMPController.class);

    public MaDMPController() {
    }

    /**
     *
     * UC1: Modify maDMP information
     *
     * @param principal
     * @param dmpScheme
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.MODIFY_MADMP_INFORMATION, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateMaDMP(Principal principal, @Valid @RequestBody DMPScheme dmpScheme) {

        // 5. UC8: Identify RDM service
        final RDMService rdmService = rdmServiceModule.findByAccessRights(principal.getName());

        // 6. UC7: Validate and identify maDMP
        dmpModule.validateAndIdentifyMaDMP(dmpScheme.getDmp(), rdmService);

        // 7. If maDMP is new
        if (dmpScheme.getDmp().isNew()) {
            // 7.1 The integration service checks whether the modified property is newer
            // than the last saved version.
            dmpModule.checkModifiedProperty(dmpScheme.getDmp());

            // 7.2
            dmpModule.update(dmpScheme.getDmp(), rdmService);
        } else {
            // 8. Else
            // 8.1 The integration service stores all properties from the received maDMP.
            dmpModule.create(dmpScheme.getDmp(), rdmService);
        }

        // 9. UC9: Synchronize changes with RDM services
        sendMaDMPToAllActiveServices(dmpScheme.getDmp(), rdmService);
    }

    /**
     *
     * UC2: Modify the maDMP object identifier
     *
     * @param principal
     * @param identifier
     * @param created
     * @param modified
     * @param property
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.UPDATE_MADMP_OBJECT_IDENTIFIER, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void changeIdentifier(final Principal principal,
            @RequestParam(required = true) String identifier,
            @RequestParam(required = true) String created,
            @RequestParam(required = true) String modified,
            @Valid @RequestBody Property property) {

        // 4. UC8: Identify RDM service
        final RDMService rdmService = rdmServiceModule.findByAccessRights(principal.getName());

        // 5. The integration service creates a maDMP representation with the received
        // values
        final DMP dmp = new DMP(created, modified, new DMP_id(identifier));

        // 6. UC7: Validate and identify maDMP
        dmpModule.validateAndIdentifyMaDMP(dmp, rdmService);

        // 7. If maDMP is new
        if (!dmp.isNew()) {
            // 7.1 The integration service returns an error message to the RDM service and
            // terminates the process.
            throw new NotFoundException("It is not possible to change the identifier for the new maDMP.");
        } else {
            // 8. Else
            // 8.1 The integration service checks whether the modified property is newer
            // than the last saved version.
            dmpModule.checkModifiedProperty(dmp);

            // 8.2 The integration service checks whether the message contains all the
            // necessary and valid information for a successful identifier change.
            if (property == null || property.getAtLocation() == null || property.getSpecializationOf() == null
                    || property.getValue() == null) {
                throw new BadRequestException("Incomplete input data.");
            }

            // 8.3
            dmpModule.changeObjectIdentifier(dmp, property, rdmService);

            // 8.4 UC9: Synchronize changes with RDM services
            sendMaDMPToAllActiveServices(dmp, rdmService);
        }

    }

    /**
     *
     * UC3: Remove the maDMP object
     *
     * @param principal
     * @param identifier
     * @param created
     * @param modified
     * @param property
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.REMOVE_MADMP_OBJECT, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteInstance(Principal principal,
            @RequestParam(required = true) String identifier,
            @RequestParam(required = true) String created,
            @RequestParam(required = true) String modified,
            @Valid @RequestBody Property property) {

        // 4. UC8: Identify RDM service
        final RDMService rdmService = rdmServiceModule.findByAccessRights(principal.getName());

        // 5. The integration service creates a maDMP representation with the received
        // values
        final DMP dmp = new DMP(created, modified, new DMP_id(identifier));

        // 6. UC7: Validate and identify maDMP
        dmpModule.validateAndIdentifyMaDMP(dmp, rdmService);

        // 7. If maDMP is new
        if (!dmp.isNew()) {
            // 7.1 The integration service returns an error message to the RDM service and
            // terminates the process.
            throw new NotFoundException("It is not possible to remove the object for the new maDMP.");
        } else {
            // 8. Else
            // 8.1 The integration service checks whether the modified property is newer
            // than the last saved version.
            dmpModule.checkModifiedProperty(dmp);

            // 8.2 The integration service checks whether the message contains all the
            // necessary and valid information to delete the object successfully.
            if (property == null || property.getAtLocation() == null ||
                    property.getSpecializationOf() == null) {
                throw new BadRequestException("Incomplete input data.");
            } else if (!property.getAtLocation().startsWith(dmp.getAtLocation(""))) {
                throw new ForbiddenException(
                        "Invalid property: cannot find atLocation in maDMP:" + property.getAtLocation());
            }

            // 8.3
            dmpModule.deleteInstance(dmp, property, rdmService);

            // 8.4 UC9: Synchronize changes with RDM services
            sendMaDMPToAllActiveServices(dmp, rdmService);
        }
    }

    /**
     *
     * UC4: Get the maDMP
     *
     * @param principal
     * @param identifier
     * @param created
     * @param version
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.GET_MADMP, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DMPScheme getMaDMP(Principal principal,
            @RequestParam(required = true) String identifier,
            @RequestParam(required = true) String created,
            @RequestParam(required = false) String version) {

        // 3. UC8: Identify RDM service
        final RDMService rdmService = rdmServiceModule.findByAccessRights(principal.getName());

        // 4. The integration service creates a maDMP representation with the received
        // values. The modified property is set to the current time
        final DMP dmp = new DMP(created, LocalDateTime.now(), new DMP_id(identifier));

        // 5. UC7: Validate and identify maDMP
        dmpModule.validateAndIdentifyMaDMP(dmp, rdmService);

        // 6. If maDMP is new
        if (!dmp.isNew()) {
            // 6.1 The integration service returns an error message to the RDM service and
            // terminates the process.
            throw new NotFoundException("It is not possible to get the maDMP for the new one.");
        } else {
            // 7.1 The integration service loads the properties based on the defined time.
            // If the parameter is null, it returns the current maDMP
            // TODO: Support older version of the maDMP based on the parameter
            return dmpModule.loadWholeDMP(dmp);
        }
    }

    /**
     *
     * UC5: Get provenance information
     *
     * @param principal
     * @param identifier
     * @param created
     * @param specializationOf
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.GET_PROVENANCE_INFORMATION, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Property> getProvenanceInformation(Principal principal,
            @RequestParam(required = true) String identifier,
            @RequestParam(required = true) String created,
            @RequestParam(required = true) String specializationOf) {

        // 3. UC8: Identify RDM service
        final RDMService rdmService = rdmServiceModule.findByAccessRights(principal.getName());

        // 4. The integration service creates a maDMP representation with the received
        // values. The modified property is set to the current time
        final DMP dmp = new DMP(created, LocalDateTime.now(), new DMP_id(identifier));

        // 5. UC7: Validate and identify maDMP
        dmpModule.validateAndIdentifyMaDMP(dmp, rdmService);

        // 6. The integration service loads all stored values for a given property of
        // the particular maDMP.
        return dmpModule.getProvenanceInformation(dmp, new Property(specializationOf));
    }

    /* Private */

    /**
     * 
     * UC9: Synchronize changes with RDM services
     * 
     * @param dmp
     * @param rdmService
     */
    @Async
    private void sendMaDMPToAllActiveServices(DMP dmp, RDMService rdmService) {
        // Set http headers
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 1. The integration service will generate a new customized maDMP with all
        // currently valid properties for the particular DMP.
        final DMPScheme dmpScheme = dmpModule.loadWholeDMP(dmp);

        // 2. The integration service loads all RDM services that are not in the
        // terminated state and did not send the change.
        // 3. For each RDM service
        for (RDMService service : rdmServiceModule.getRDMServices(true)) {
            // 2. (Except from which information received)
            if (!service.equals(rdmService)) {
                try {
                    // 3.1 The integration service sends a message with the generated maDMP to the
                    // RDM service.
                    final HttpEntity<DMPScheme> request = new HttpEntity<>(dmpScheme, headers);
                    final RestTemplate restTemplate = new RestTemplate();
                    restTemplate.exchange(service.getEndpointURL(), HttpMethod.PUT, request, String.class);
                    log.debug("Sending a new version of maDMP to " + service.getEndpointURL());

                    // 3.3.1 If the service RDM state is unavailable
                    if (service.getState().equals(RDMServiceState.UNAVAILABLE)) {
                        // 3.3.1.1 The integration service changes the state of the RDM service to
                        // unsynchronized
                        rdmServiceModule.setState(service, RDMServiceState.UNSYNCHRONIZED);
                    }

                    // 3.3.2 If the service RDM state is unsynchronized
                    if (service.getState().equals(RDMServiceState.UNSYNCHRONIZED)) {
                        // 3.3.2.1 UC10 Synchronize all maDMPs
                        synchronizeAllMaDMP(service);
                    }

                } catch (RestClientException ex) {
                    // 3.2 If the RDM service is not available
                    log.error("Error when sending maDMP to service: " + ex.getMessage());

                    // 3.2.1 The integration service changes the state of the RDM service to
                    // unavailable
                    rdmServiceModule.setState(service, RDMServiceState.UNAVAILABLE);
                }
            }
        }
    }

    /**
     * 
     * UC10: Synchronize all maDMPs
     * 
     * @param rdmService
     */
    @Async
    private void synchronizeAllMaDMP(RDMService rdmService) {
        // 1. The integration service loads all maDMP identifiers.
        final List<Property> identifiers = dmpModule.getAllMaDMPs();

        // 2. For each maDMP identifier
        for (Property identifier : identifiers) {

            // Set http headers
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 2.1 The integration service will generate a new customized maDMP with all
            // currently valid properties for the particular maDMP
            final DMPScheme dmpScheme = dmpModule
                    .loadWholeDMP(new DMP(LocalDateTime.now(), LocalDateTime.now(), new DMP_id(identifier.getValue())));

            try {
                // 2.2 The integration service sends a message with the generated maDMP to the
                // RDM service
                final HttpEntity<DMPScheme> request = new HttpEntity<>(dmpScheme, headers);
                final RestTemplate restTemplate = new RestTemplate();
                restTemplate.exchange(rdmService.getEndpointURL(), HttpMethod.PUT, request, String.class);
                log.debug("Sending a new version of maDMP to " + rdmService.getEndpointURL());
            } catch (RestClientException ex) {
                // 2.3 If the RDM service is not available
                log.error("Error when sending maDMP to service: " + ex.getMessage());

                // 2.3.1 The integration service changes the state of the RDM service to
                // unavailable
                rdmServiceModule.setState(rdmService, RDMServiceState.UNAVAILABLE);

                // 2.3.2 The integration service terminates the process.
                break;
            }

        }

        // 3. The integration service changes the state of the RDM serviceto active
        rdmServiceModule.setState(rdmService, RDMServiceState.ACTIVE);
    }

}
