package at.tuwien.simulation.service;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import at.tuwien.simulation.model.Entity;
import at.tuwien.simulation.model.dmp.DMP;
import at.tuwien.simulation.model.dmp.DMPScheme;
import at.tuwien.simulation.model.dmp.DMP_id;
import at.tuwien.simulation.model.test.TestCaseEntity;
import at.tuwien.simulation.util.Functions;

public abstract class AbstractTestCaseService {

    @Value("${indmp.host}")
    protected String indmpHost;

    @Value("${indmp.endpoint.update.madmp}")
    protected String indmpUpdateMaDMP;

    @Value("${indmp.endpoint.update.identifier}")
    protected String indmpUpdateIdentifier;

    @Value("${indmp.endpoint.delete.instance}")
    protected String indmpDeleteInstance;

    @Value("${indmp.endpoint.get.identifiers}")
    protected String indmpGetIdentifiers;

    @Value("${indmp.endpoint.exists.service}")
    protected String indmpExistsService;

    @Value("${indmp.endpoint.get.madmp}")
    protected String indmpGetMaDMP;

    @Autowired
    protected CommonService commonService;

    private final Logger log = LoggerFactory.getLogger(AbstractTestCaseService.class);

    public void checkEntryConditions(OAuth2AuthorizedClient authorizedClient) {
        log.info("Checking entry conditions...");

        // Check if the client is successfully logged
        if (authorizedClient == null || authorizedClient.getAccessToken() == null
                || authorizedClient.getAccessToken().getTokenValue() == null ||
                !authorizedClient.getAccessToken().getScopes().contains("update")) {
            throw new ValidationException("Token is invalid.");
        }

        // Check if service is registered
        final HttpEntity<String> request = new HttpEntity<>("", Functions.getHeaders(authorizedClient));
        final ResponseEntity<String> restTemplate = Functions.sendHTTPRequest(log, indmpHost + indmpExistsService,
                HttpMethod.GET, request, String.class);
        // If not registered, register
        if (!Boolean.parseBoolean(restTemplate.getBody())) {
            try {
                commonService.registerService(authorizedClient);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract TestCaseEntity executeTestSteps(OAuth2AuthorizedClient authorizedClient);

    public abstract String validate(OAuth2AuthorizedClient authorizedClient, TestCaseEntity testCaseEntity);

    /**
     * 
     * Create a minimal DMP scheme
     * 
     * @param sameDate
     * @param fullDMP  can be null
     * @return
     */
    public DMPScheme createMinimalDMPScheme(boolean sameDate) {
        log.info("Creating minimal DMP scheme...");

        // Get create, modified, identifier
        Date modified = new Date();
        final Date created = new Date(modified.getTime() - 60000);
        final String identifier = "DMP_" + Functions.getRandomNumberBetween(1, 20000);

        // Should be create and modified same?
        if (sameDate) {
            modified = created;
        }

        // Create dmp scheme
        final DMPScheme dmpScheme = new DMPScheme();
        final DMP dmp = new DMP(created, modified, new DMP_id(identifier));
        dmpScheme.setDmp(dmp);

        return dmpScheme;
    }

    /**
     * 
     * Validate if dmp are same
     * 
     * @param dmpScheme1
     * @param dmpScheme2
     */
    public void validateDMPScheme(DMPScheme dmpScheme1, DMPScheme dmpScheme2) {
        Objects.requireNonNull(dmpScheme1, "DMP Scheme 1 is null");
        Objects.requireNonNull(dmpScheme2, "DMP Scheme 2 is null");
        Objects.requireNonNull(dmpScheme1.getDmp(), "DMP in scheme 1 is null");
        Objects.requireNonNull(dmpScheme2.getDmp(), "DMP in scheme 2 is null");

        log.info("Validating DMP scheme...");

        // Get all properties
        final List<Entity> properties1 = dmpScheme1.getDmp().getProperties(dmpScheme1.getDmp(), "");
        properties1.addAll(dmpScheme1.getDmp().getPropertiesFromNestedClasses(dmpScheme1.getDmp(), ""));

        final List<Entity> properties2 = dmpScheme2.getDmp().getProperties(dmpScheme2.getDmp(), "");
        properties2.addAll(dmpScheme2.getDmp().getPropertiesFromNestedClasses(dmpScheme2.getDmp(), ""));

        // Validate each property
        for (final Entity property : properties1) {
            Functions.validateProperty(property, properties2);
        }

    }
}
