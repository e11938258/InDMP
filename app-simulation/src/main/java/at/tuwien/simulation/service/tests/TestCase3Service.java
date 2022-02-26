package at.tuwien.simulation.service.tests;

import java.util.Arrays;
import java.util.Date;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;

import at.tuwien.simulation.model.dmp.Contact;
import at.tuwien.simulation.model.dmp.Contact_id;
import at.tuwien.simulation.model.dmp.Cost;
import at.tuwien.simulation.model.dmp.DMPScheme;
import at.tuwien.simulation.model.test.TestCaseEntity;
import at.tuwien.simulation.service.AbstractTestCaseService;
import at.tuwien.simulation.util.Functions;

@Service
public class TestCase3Service extends AbstractTestCaseService {

    private final Logger log = LoggerFactory.getLogger(TestCase3Service.class);

    @Override
    public TestCaseEntity executeTestSteps(OAuth2AuthorizedClient authorizedClient) {
        log.info("Executing test steps...");

        // Create test case entity
        final TestCaseEntity testCaseEntity = new TestCaseEntity();

        // Create a new DMP Scheme
        final DMPScheme dmpScheme = createMinimalDMPScheme(true);

        // DMP
        dmpScheme.getDmp().setEthical_issues_exist("no");
        dmpScheme.getDmp().setLanguage("eng");
        dmpScheme.getDmp().setTitle("DMP for a new project");
        dmpScheme.getDmp().getDmp_id().setType("doi");

        // Contact
        dmpScheme.getDmp().setContact(new Contact("john.smith@tuwien.ac.at", "John Smith",
                new Contact_id("https://www.tiss.tuwien.ac.at/person/2351952424", "other")));

        // Cost - not in modification scope
        dmpScheme.getDmp().setCost(Arrays
                .asList(new Cost[] { new Cost("CZK", "Project budget description", "Project budget", 120000.0) }));

        // Set request with body
        testCaseEntity.setDmpScheme(dmpScheme);
        final HttpEntity<DMPScheme> request = new HttpEntity<>(dmpScheme, Functions.getHeaders(authorizedClient));

        // Send request
        final ResponseEntity<String> responseEntity = Functions.sendHTTPRequest(log, indmpHost + indmpUpdateMaDMP,
                HttpMethod.PUT, request, String.class);
        testCaseEntity.setStatusCode(responseEntity.getStatusCode().toString());

        return testCaseEntity;
    }

    @Override
    public String validate(OAuth2AuthorizedClient authorizedClient, TestCaseEntity testCaseEntity) {
        log.info("Validating results..");
        
        // Get current maDMP from InDMP
        final HttpEntity<String> request = new HttpEntity<>("", Functions.getHeaders(authorizedClient));
        final ResponseEntity<DMPScheme> reponse = Functions.sendHTTPRequest(log,
                indmpHost + indmpGetMaDMP
                        + Functions.getDMPParameters(testCaseEntity.getDmpScheme().getDmp(), new Date()),
                HttpMethod.GET, request, DMPScheme.class);

        // Are same?
        try {
            validateDMPScheme(testCaseEntity.getDmpScheme(), reponse.getBody());
        } catch (ValidationException ex) {
            return Functions.processSuccess(log, "Service created maDMP in its modification scope");
        }
        return Functions.processError(log, "Service created the whole maDMP!");
        
    }

}
