package at.tuwien.indmp.controller;

import java.security.Principal;

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
    @RequestMapping(value = Endpoints.UPDATE_MADMP, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
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
        sendDMPToServices(dmpScheme.getDmp(), rdmService);
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
    @RequestMapping(value = Endpoints.UPDATE_MADMP_IDENTIFIER, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
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
            sendDMPToServices(dmp, rdmService);
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
    @RequestMapping(value = Endpoints.DELETE_MADMP_INSTANCE, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
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
            sendDMPToServices(dmp, rdmService);
        }
    }

    // /**
    // *
    // * Get maDMP
    // *
    // * @param principal
    // * @param identifier
    // * @param created
    // * @param modified
    // * @return
    // */
    // @ResponseStatus(HttpStatus.OK)
    // @RequestMapping(value = Endpoints.GET_MADMP, method = RequestMethod.GET,
    // produces = MediaType.APPLICATION_JSON_VALUE)
    // public DMPScheme getMaDMP(Principal principal,
    // @RequestParam(required = true) String identifier,
    // @RequestParam(required = true) String created,
    // @RequestParam(required = true) @JsonFormat(pattern =
    // ModelConstants.DATE_TIME_FORMAT_ISO_8601) String modified) {

    // // Get current RDM Service
    // rdmServiceModule.findByAccessRights(principal.getName());

    // try {
    // // If modified is null...
    // LocalDateTime modifiedProperty;
    // if (modified == null) {
    // modifiedProperty = LocalDateTime.now();
    // } else {
    // modifiedProperty = LocalDateTime.parse(modified);
    // }

    // // Identify maDMP
    // final DMP dmpMinimum = dmpModule.validateAndIdentifyMaDMP(new DMP(created,
    // modifiedProperty, new DMP_id(identifier)),
    // null);

    // // Return current version of maDMP if found
    // if (dmpMinimum != null) {
    // return dmpModule.loadWholeDMP(dmpMinimum);
    // } else {
    // throw new NotFoundException("DMP not found, identifier: " + identifier);
    // }
    // } catch (DateTimeParseException ex) {
    // throw new BadRequestException("Wrong time format");
    // }
    // }

    // /**
    // *
    // * Get history of identifiers
    // *
    // * @param principal
    // * @param identifier
    // * @param created
    // * @return
    // */
    // @ResponseStatus(HttpStatus.OK)
    // @ResponseBody
    // @RequestMapping(value = Endpoints.GET_MADMP_IDENTIFIERS, method =
    // RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    // public List<Property> getIdentifierHistory(Principal principal,
    // @RequestParam(required = true) String identifier,
    // @RequestParam(required = true) String created) {

    // // Get current RDM service
    // final RDMService dataService =
    // rdmServiceModule.findByAccessRights(principal.getName());

    // // Identify maDMP
    // final DMP currentDMP = dmpModule.validateAndIdentifyMaDMP(new DMP(created,
    // LocalDateTime.now(), new DMP_id(identifier)),
    // dataService);

    // // Return identifier history if found
    // if (currentDMP != null) {
    // return dmpModule.loadIdentifierHistory(currentDMP);
    // } else {
    // throw new NotFoundException("DMP not found, identifier: " + identifier);
    // }
    // }

    /* Private */

    @Async
    private void sendDMPToServices(DMP dmp, RDMService dataService) {
        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Load whole dmp
        final DMPScheme dmpScheme = dmpModule.loadWholeDMP(dmp);

        // For each service
        for (RDMService service : rdmServiceModule.getAllRDMServices()) {
            // Except from which information received
            if (!service.equals(dataService)) {
                try {
                    log.info("Sending a new version of maDMP to " + service.getEndpointURL());
                    final HttpEntity<DMPScheme> request = new HttpEntity<>(dmpScheme, headers);
                    final RestTemplate restTemplate = new RestTemplate();
                    restTemplate.exchange(service.getEndpointURL(), HttpMethod.PUT, request, String.class);
                } catch (RestClientException ex) {
                    log.error("Error when sending maDMP to service: " + ex.getMessage());
                }
            }
        }
    }

}
