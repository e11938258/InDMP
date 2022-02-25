package at.tuwien.simulation.service.tests;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;

import at.tuwien.simulation.model.dmp.DMPScheme;
import at.tuwien.simulation.model.test.TestCaseEntity;
import at.tuwien.simulation.service.AbstractTestCaseService;
import at.tuwien.simulation.util.Functions;

@Service
public class TestCase1Service extends AbstractTestCaseService {

    private final Logger log = LoggerFactory.getLogger(TestCase1Service.class);

    @Override
    public TestCaseEntity executeTestSteps(OAuth2AuthorizedClient authorizedClient) {
        // Create test case entity
        final TestCaseEntity testCaseEntity = new TestCaseEntity();

        // Set request with body
        final DMPScheme dmpScheme = createMinimalDMPScheme(true);
        testCaseEntity.setDmpScheme(dmpScheme);
        final HttpEntity<DMPScheme> request = new HttpEntity<>(dmpScheme, Functions.getHeaders(authorizedClient));

        // Send request
        final ResponseEntity<String> responseEntity = Functions.sendHTTPRequest(indmpHost + indmpUpdateMaDMP,
                HttpMethod.PUT, request, String.class);
        testCaseEntity.setStatusCode(responseEntity.getStatusCode().toString());
        testCaseEntity.setBody(responseEntity.getBody().toString());

        return testCaseEntity;
    }

    @Override
    public String validate(OAuth2AuthorizedClient authorizedClient, TestCaseEntity testCaseEntity) {
        // Get current maDMP
        final HttpEntity<String> request = new HttpEntity<>("", Functions.getHeaders(authorizedClient));
        final ResponseEntity<DMPScheme> reponse = Functions.sendHTTPRequest(
                indmpHost + indmpGetMaDMP
                        + Functions.getDMPParameters(testCaseEntity.getDmpScheme().getDmp(), new Date()),
                HttpMethod.GET, request, DMPScheme.class);

        // Are same?
        validateDMPScheme(testCaseEntity.getDmpScheme(), reponse.getBody());

        return Functions.processSuccess(log, "maDMP was created");
    }

}
