package at.tuwien.simulation.service.tests;

import java.net.URI;
import java.net.URISyntaxException;
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

import at.tuwien.simulation.model.Entity;
import at.tuwien.simulation.model.dmp.*;
import at.tuwien.simulation.model.dmp.DMPScheme;
import at.tuwien.simulation.model.test.TestCaseEntity;
import at.tuwien.simulation.service.AbstractTestCaseService;
import at.tuwien.simulation.util.Functions;

@Service
public class TestCase6Service extends AbstractTestCaseService {

    private final Logger log = LoggerFactory.getLogger(TestCase6Service.class);

    @Override
    public TestCaseEntity executeTestSteps(OAuth2AuthorizedClient authorizedClient) {
        log.info("Executing test steps...");

        // Create test case entity
        final TestCaseEntity testCaseEntity = new TestCaseEntity();

        // Create a new DMP Scheme
        final DMPScheme dmpScheme = createMinimalDMPScheme(true);
        // DMP
        dmpScheme.getDmp().setTitle("DMP for a new project");
        dmpScheme.getDmp().setDescription("DMP descriptions");
        dmpScheme.getDmp().setEthical_issues_exist("no");
        dmpScheme.getDmp().setLanguage("eng");

        // Dataset
        try {
            dmpScheme.getDmp().setDataset(
                    Arrays.asList(new Dataset[] {
                            new Dataset(
                                    Arrays.asList(new String[] {}),
                                    "Some test scripts", new Date(),
                                    Arrays.asList(new String[] { "client", "application" }),
                                    "eng", "no", null, "no", "Client application", "Source code",
                                    // Dataset id
                                    new Dataset_id("https://hdl.handle.net/0000/00.00000", "handle"),
                                    // Distributions
                                    Arrays.asList(new Distribution[] {
                                            new Distribution(
                                                    new URI("http://github.com/1234"),
                                                    null, 100025L, "open", "Description", null,
                                                    Arrays.asList(new String[] { "java" }),
                                                    "Planned distribution",
                                                    // Host
                                                    new Host("99.9", null, null, null, "GitHub repository", null,
                                                            Arrays.asList(new String[] { "other" }),
                                                            "repository", "yes", "Github",
                                                            new URI("https://www.re3data.org/repository/r3d100010375")),
                                                    // License
                                                    Arrays.asList(new License[] {
                                                            new License(
                                                                    new URI("http://opensource.org/licenses/mit-license.php"),
                                                                    new Date())
                                                    }))
                                    }),
                                    Arrays.asList(new Metadata[] {}),
                                    Arrays.asList(new SecurityAndPrivacy[] {}),
                                    Arrays.asList(new TechnicalResource[] {})),
                            new Dataset(
                                    Arrays.asList(new String[] {}),
                                    "Some test scripts 2", new Date(), Arrays.asList(new String[] {}), "eng", "no",
                                    null, "no", "Client application", "Source code",
                                    // Dataset id
                                    new Dataset_id("https://hdl.handle.net/0432/00.13000", "handle"),
                                    // Distributions
                                    Arrays.asList(new Distribution[] {
                                            new Distribution(
                                                    new URI("http://github.com/0643"),
                                                    null, 4315L, "open", "Description of distribution", null,
                                                    Arrays.asList(new String[] { "java" }),
                                                    "Planned distribution 2",
                                                    // Host
                                                    new Host("99.9", null, null, null,
                                                            "GitHub repository", null,
                                                            Arrays.asList(new String[] { "other" }),
                                                            "repository", "yes", "Github",
                                                            new URI("https://www.re3data.org/repository/r3d100010375")),
                                                    // License
                                                    Arrays.asList(new License[] {
                                                            new License(
                                                                    new URI("http://opensource.org/licenses/mit-license.php"),
                                                                    new Date())
                                                    }))
                                    }),
                                    Arrays.asList(new Metadata[] {}),
                                    Arrays.asList(new SecurityAndPrivacy[] {}),
                                    Arrays.asList(new TechnicalResource[] {}))
                    }));
        } catch (URISyntaxException e) {
            throw new ValidationException("URI is not valid!");
        }

        // Set request with body
        final HttpEntity<DMPScheme> request = new HttpEntity<>(dmpScheme, Functions.getHeaders(authorizedClient));
        // Send request
        Functions.sendHTTPRequest(log, indmpHost + indmpUpdateMaDMP, HttpMethod.PUT, request, String.class);

        // Delete dataset instance
        // Create a new entity with identifier
        final Dataset dataset = dmpScheme.getDmp().getDataset().get(0);
        final Entity entity = new Entity(dataset.getLocation(dmpScheme.getDmp().getLocation("")),
                null, null);
        final HttpEntity<Entity> request2 = new HttpEntity<>(entity, Functions.getHeaders(authorizedClient));

        // Send request
        final ResponseEntity<String> responseEntity2 = Functions.sendHTTPRequest(log,
                indmpHost + indmpDeleteInstance + Functions.getDMPParameters(dmpScheme.getDmp(), new Date()),
                HttpMethod.PUT, request2, String.class);
        testCaseEntity.setStatusCode(responseEntity2.getStatusCode().toString());
        testCaseEntity.setDmpScheme(dmpScheme);

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
            return Functions.processSuccess(log, "Dataset instance was deleted");
        }
        return Functions.processError(log, "Dataset instnace was not deleted!");
    }

}
