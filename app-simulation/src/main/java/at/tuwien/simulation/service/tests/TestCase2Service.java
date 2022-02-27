package at.tuwien.simulation.service.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import at.tuwien.simulation.model.dmp.DMPScheme;
import at.tuwien.simulation.model.test.TestCaseEntity;
import at.tuwien.simulation.service.AbstractTestCaseService;
import at.tuwien.simulation.util.Functions;

@Service
public class TestCase2Service extends AbstractTestCaseService {

    private final Logger log = LoggerFactory.getLogger(TestCase2Service.class);

    @Override
    public TestCaseEntity executeTestSteps(OAuth2AuthorizedClient authorizedClient) {
        log.info("Executing test steps...");

        // Create test case entity
        final TestCaseEntity testCaseEntity = new TestCaseEntity();

        // Set request with body
        final DMPScheme dmpScheme = createMinimalDMPScheme(true);
        // Remove identifier
        dmpScheme.getDmp().getDmp_id().setIdentifier("");
        testCaseEntity.setDmpScheme(dmpScheme);
        final HttpEntity<DMPScheme> request = new HttpEntity<>(dmpScheme, Functions.getHeaders(authorizedClient));

        // Send request
        try {
            Functions.sendHTTPRequest(log, indmpHost + indmpUpdateMaDMP, HttpMethod.PUT, request, String.class);
        } catch (HttpClientErrorException ex) {
            testCaseEntity.setStatusCode(ex.getStatusCode().toString());
        }

        return testCaseEntity;
    }

    @Override
    public String validate(OAuth2AuthorizedClient authorizedClient, TestCaseEntity testCaseEntity) {
        log.info("Validating results..");

        // Return correct status code?
        if (!testCaseEntity.getStatusCode().equals("400 BAD_REQUEST")) {
            return Functions.processError(log,
                    "InDMP returned wrong status code - " + testCaseEntity.getStatusCode());
        } else {
            return Functions.processSuccess(log, "InDMP returned status code 400.");
        }
    }

}
