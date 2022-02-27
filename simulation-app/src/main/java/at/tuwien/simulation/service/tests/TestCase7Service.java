package at.tuwien.simulation.service.tests;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;

import at.tuwien.simulation.model.test.TestCaseEntity;
import at.tuwien.simulation.service.AbstractTestCaseService;
import at.tuwien.simulation.util.Functions;

@Service
public class TestCase7Service extends AbstractTestCaseService {

    @Autowired
    private TestCase5Service testCase5;

    private final Logger log = LoggerFactory.getLogger(TestCase7Service.class);

    @Override
    public TestCaseEntity executeTestSteps(OAuth2AuthorizedClient authorizedClient) {
        // Send dmp and change identifier of instance
        final TestCaseEntity testCaseEntity = testCase5.executeTestSteps(authorizedClient);

        // Get identifiers
        final HttpEntity<String> request = new HttpEntity<>("", Functions.getHeaders(authorizedClient));
        final ResponseEntity<String> responseEntity = Functions.sendHTTPRequest(log,
                indmpHost + indmpGetIdentifiers
                        + Functions.getDMPParameters(testCaseEntity.getDmpScheme().getDmp(), new Date()),
                HttpMethod.GET, request, String.class);
        testCaseEntity.setStatusCode(responseEntity.getStatusCode().toString());
        testCaseEntity.setBody(responseEntity.getBody());

        return testCaseEntity;
    }

    @Override
    public String validate(OAuth2AuthorizedClient authorizedClient, TestCaseEntity testCaseEntity) {
        log.info(testCaseEntity.getStatusCode());
        if(testCaseEntity.getStatusCode().equals("200 OK")) {
            return Functions.processSuccess(log, "Array of returned identifiers:\n" + testCaseEntity.getBody());
        } else {
            return Functions.processError(log, "InDMP retured wrong code!");
        }
    }

}
