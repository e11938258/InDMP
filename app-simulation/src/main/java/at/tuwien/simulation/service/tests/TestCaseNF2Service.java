package at.tuwien.simulation.service.tests;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;

import at.tuwien.simulation.model.test.TestCaseEntity;
import at.tuwien.simulation.service.AbstractTestCaseService;
import at.tuwien.simulation.util.Functions;

@Service
public class TestCaseNF2Service extends AbstractTestCaseService {

    @Autowired
    private TestCase4Service testCase4;

    private final Logger log = LoggerFactory.getLogger(TestCaseNF2Service.class);

    @Override
    public TestCaseEntity executeTestSteps(OAuth2AuthorizedClient authorizedClient) {
        // Send long dmp
        return testCase4.executeTestSteps(authorizedClient);
    }

    @Override
    public String validate(OAuth2AuthorizedClient authorizedClient, TestCaseEntity testCaseEntity) {
        return Functions.processSuccess(log, "Minimal maDMP was created in time: "
                + (new Date().getTime() - testCaseEntity.getStartDate().getTime()) + "ms");
    }

}
