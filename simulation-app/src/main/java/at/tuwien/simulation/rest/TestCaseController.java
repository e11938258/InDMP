package at.tuwien.simulation.rest;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import at.tuwien.simulation.model.test.TestCaseEntity;
import at.tuwien.simulation.service.tests.*;
import at.tuwien.simulation.util.Endpoints;
import at.tuwien.simulation.util.Functions;

@RestController
public class TestCaseController {

    @Autowired
    private TestCase1Service testCase1;

    @Autowired
    private TestCase2Service testCase2;

    @Autowired
    private TestCase3Service testCase3;

    @Autowired
    private TestCase4Service testCase4;

    @Autowired
    private TestCase5Service testCase5;

    @Autowired
    private TestCase6Service testCase6;

    @Autowired
    private TestCase7Service testCase7;

    @Autowired
    private TestCaseNF1Service testCaseNF1;

    @Autowired
    private TestCaseNF2Service testCaseNF2;

    @Autowired
    private Environment environment;

    private final Logger log = LoggerFactory.getLogger(TestCaseController.class);

    /**
     * 
     * Test case 1
     * 
     * @throws URISyntaxException
     * 
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.TEST_CASE_1, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String executeTestCase1(
            @RegisteredOAuth2AuthorizedClient("indmp-client") OAuth2AuthorizedClient authorizedClient) {
        try {
            // Check entry conditions
            testCase1.checkEntryConditions(authorizedClient);

            // Execute test steps
            final TestCaseEntity testCaseEntity = testCase1.executeTestSteps(authorizedClient);

            // Validate
            return testCase1.validate(authorizedClient, testCaseEntity);
        } catch (Throwable e) {
            return Functions.processError(log, e.toString());
        }
    }

    /**
     * 
     * Test case 2
     * 
     * @throws URISyntaxException
     * 
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.TEST_CASE_2, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String executeTestCase2(
            @RegisteredOAuth2AuthorizedClient("indmp-client") OAuth2AuthorizedClient authorizedClient) {
        try {
            // Check entry conditions
            testCase2.checkEntryConditions(authorizedClient);

            // Execute test steps
            final TestCaseEntity testCaseEntity = testCase2.executeTestSteps(authorizedClient);

            // Validate
            return testCase2.validate(authorizedClient, testCaseEntity);
        } catch (Throwable e) {
            return Functions.processError(log, e.toString());
        }
    }

    /**
     * 
     * Test case 3
     * 
     * @throws URISyntaxException
     * 
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.TEST_CASE_3, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String executeTestCase3(
            @RegisteredOAuth2AuthorizedClient("indmp-client") OAuth2AuthorizedClient authorizedClient) {
        // Repository app only
        if (environment.getActiveProfiles().length > 0 && environment.getActiveProfiles()[0].equals("repository")) {
            try {
                // Check entry conditions
                testCase3.checkEntryConditions(authorizedClient);

                // Execute test steps
                final TestCaseEntity testCaseEntity = testCase3.executeTestSteps(authorizedClient);

                // Validate
                return testCase3.validate(authorizedClient, testCaseEntity);
            } catch (Throwable e) {
                return Functions.processError(log, e.toString());
            }
        } else {
            return Functions.processError(log, "Only repository app can execute the test case!");
        }
    }

    /**
     * 
     * Test case 4
     * 
     * @throws URISyntaxException
     * 
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.TEST_CASE_4, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String executeTestCase4(
            @RegisteredOAuth2AuthorizedClient("indmp-client") OAuth2AuthorizedClient authorizedClient) {
        // DMP app only
        if (environment.getActiveProfiles().length > 0 && environment.getActiveProfiles()[0].equals("repository")) {
            return Functions.processError(log, "Only dmp app can execute the test case!");
        } else {
            try {
                // Check entry conditions
                testCase4.checkEntryConditions(authorizedClient);

                // Execute test steps
                final TestCaseEntity testCaseEntity = testCase4.executeTestSteps(authorizedClient);

                // Validate
                return testCase4.validate(authorizedClient, testCaseEntity);
            } catch (Throwable e) {
                return Functions.processError(log, e.toString());
            }
        }
    }

    /**
     * 
     * Test case 5
     * 
     * @throws URISyntaxException
     * 
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.TEST_CASE_5, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String executeTestCase5(
            @RegisteredOAuth2AuthorizedClient("indmp-client") OAuth2AuthorizedClient authorizedClient) {
        try {
            // Check entry conditions
            testCase5.checkEntryConditions(authorizedClient);

            // Execute test steps
            final TestCaseEntity testCaseEntity = testCase5.executeTestSteps(authorizedClient);

            // Validate
            return testCase5.validate(authorizedClient, testCaseEntity);
        } catch (Throwable e) {
            return Functions.processError(log, e.toString());
        }
    }

    /**
     * 
     * Test case 6
     * 
     * @throws URISyntaxException
     * 
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.TEST_CASE_6, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String executeTestCase6(
            @RegisteredOAuth2AuthorizedClient("indmp-client") OAuth2AuthorizedClient authorizedClient) {
        try {
            // Check entry conditions
            testCase6.checkEntryConditions(authorizedClient);

            // Execute test steps
            final TestCaseEntity testCaseEntity = testCase6.executeTestSteps(authorizedClient);

            // Validate
            return testCase6.validate(authorizedClient, testCaseEntity);
        } catch (Throwable e) {
            return Functions.processError(log, e.toString());
        }
    }

    /**
     * 
     * Test case 7
     * 
     * @throws URISyntaxException
     * 
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.TEST_CASE_7, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String executeTestCase7(
            @RegisteredOAuth2AuthorizedClient("indmp-client") OAuth2AuthorizedClient authorizedClient) {
        try {
            // Check entry conditions
            testCase7.checkEntryConditions(authorizedClient);

            // Execute test steps
            final TestCaseEntity testCaseEntity = testCase7.executeTestSteps(authorizedClient);

            // Validate
            return testCase7.validate(authorizedClient, testCaseEntity);
        } catch (Throwable e) {
            return Functions.processError(log, e.toString());
        }
    }

    /**
     * 
     * Test case NF 1
     * 
     * @throws URISyntaxException
     * 
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.NF_TEST_CASE_1, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String executeTestCaseNF1(
            @RegisteredOAuth2AuthorizedClient("indmp-client") OAuth2AuthorizedClient authorizedClient) {
        try {
            // Check entry conditions
            testCaseNF1.checkEntryConditions(authorizedClient);

            // Execute test steps
            final TestCaseEntity testCaseEntity = testCaseNF1.executeTestSteps(authorizedClient);

            // Validate
            return testCaseNF1.validate(authorizedClient, testCaseEntity);
        } catch (Throwable e) {
            return Functions.processError(log, e.toString());
        }
    }

    /**
     * 
     * Test case NF 2
     * 
     * @throws URISyntaxException
     * 
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.NF_TEST_CASE_2, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String executeTestCaseNF2(
            @RegisteredOAuth2AuthorizedClient("indmp-client") OAuth2AuthorizedClient authorizedClient) {
        try {
            // Check entry conditions
            testCaseNF2.checkEntryConditions(authorizedClient);

            // Execute test steps
            final TestCaseEntity testCaseEntity = testCaseNF2.executeTestSteps(authorizedClient);

            // Validate
            return testCaseNF2.validate(authorizedClient, testCaseEntity);
        } catch (Throwable e) {
            return Functions.processError(log, e.toString());
        }
    }

}
