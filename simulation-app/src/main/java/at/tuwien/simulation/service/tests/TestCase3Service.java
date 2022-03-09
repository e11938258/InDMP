package at.tuwien.simulation.service.tests;

import java.time.LocalDateTime;
import java.util.Arrays;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;

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

        // Set request with body
        final HttpEntity<DMPScheme> request = new HttpEntity<>(dmpScheme, Functions.getHeaders(authorizedClient));

        // Send request
        final ResponseEntity<String> responseEntity = Functions.sendHTTPRequest(log, indmpHost + indmpUpdateMaDMP,
                HttpMethod.PUT, request, String.class);
        testCaseEntity.setStatusCode(responseEntity.getStatusCode().toString());

        // Send second request with modifications

        // Set some DMP properties
        dmpScheme.getDmp().setModified(LocalDateTime.now());
        //  Not in modification scope
        dmpScheme.getDmp().setEthical_issues_exist("no");
        dmpScheme.getDmp().setLanguage("eng");
        dmpScheme.getDmp().setTitle("DMP for a new project");
        dmpScheme.getDmp().getDmp_id().setType("doi");

        // Cost - not in modification scope
        dmpScheme.getDmp().setCost(Arrays
                .asList(new Cost[] { new Cost("CZK", "Project budget description", "Project budget", 120000.0) }));

        // Set request with body
        testCaseEntity.setDmpScheme(dmpScheme);
        final HttpEntity<DMPScheme> request2 = new HttpEntity<>(dmpScheme, Functions.getHeaders(authorizedClient));

        // Send request
        final ResponseEntity<String> responseEntity2 = Functions.sendHTTPRequest(log, indmpHost + indmpUpdateMaDMP,
                HttpMethod.PUT, request2, String.class);
        testCaseEntity.setStatusCode(responseEntity2.getStatusCode().toString());

        return testCaseEntity;
    }

    @Override
    public String validate(OAuth2AuthorizedClient authorizedClient, TestCaseEntity testCaseEntity) {
        log.info("Validating results..");
        
        // Get current maDMP from InDMP
        final HttpEntity<String> request = new HttpEntity<>("", Functions.getHeaders(authorizedClient));
        final ResponseEntity<DMPScheme> reponse = Functions.sendHTTPRequest(log,
                indmpHost + indmpGetMaDMP
                        + Functions.getDMPParameters(testCaseEntity.getDmpScheme().getDmp(), LocalDateTime.now()),
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
