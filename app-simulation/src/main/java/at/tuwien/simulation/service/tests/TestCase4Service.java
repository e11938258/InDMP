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

import at.tuwien.simulation.model.dmp.*;
import at.tuwien.simulation.model.test.TestCaseEntity;
import at.tuwien.simulation.service.AbstractTestCaseService;
import at.tuwien.simulation.util.Functions;

@Service
public class TestCase4Service extends AbstractTestCaseService {

    private final Logger log = LoggerFactory.getLogger(TestCase4Service.class);

    @Override
    public TestCaseEntity executeTestSteps(OAuth2AuthorizedClient authorizedClient) {
        // Create test case entity
        final TestCaseEntity testCaseEntity = new TestCaseEntity();

        // Create a new DMP Scheme
        final DMPScheme dmpScheme = createMinimalDMPScheme(true);
        // DMP
        dmpScheme.getDmp().setTitle("DMP for a new project");
        dmpScheme.getDmp().setDescription("DMP descriptions");
        dmpScheme.getDmp().setEthical_issues_exist("no");
        dmpScheme.getDmp().setLanguage("eng");
        // DMP ID
        dmpScheme.getDmp().getDmp_id().setType("doi");
        // Contact
        dmpScheme.getDmp().setContact(new Contact("john.smith@tuwien.ac.at", "John Smith",
                new Contact_id("https://www.tiss.tuwien.ac.at/person/2351952424", "other")));
        // Cost
        dmpScheme.getDmp().setCost(Arrays
                .asList(new Cost[] { new Cost("CZK", "Project budget description", "Project budget", 120000.0) }));
        // Contributors
        dmpScheme.getDmp().setContributor(
                Arrays.asList(new Contributor[] {
                        new Contributor("leo.messi@barcelona.com", "Leo Messi",
                                Arrays.asList(new String[] { "ProjectLeader" }),
                                new Contributor_id("https://orcid.org/0000-0002-0000-0000", "orcid")),
                        new Contributor("robert@bayern.de", "Robert Lewandowski",
                                Arrays.asList(new String[] { "DataManager", "ContactPerson" }),
                                new Contributor_id("https://orcid.org/0000-0002-4929-7875", "orcid")) }));
        // Project
        dmpScheme.getDmp().setProject(
                Arrays.asList(new Project[] {
                        new Project("Project description...", null, new Date(), "openEO",
                                Arrays.asList(new Funding[] {
                                        // Funding + grant id
                                        new Funding("European Commission - Framework Programme",
                                                new Funder_id("Funder-154", "other"),
                                                new Grant_id("EO-2-2017", "other"))
                                }))
                }));
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
                                    Arrays.asList(new TechnicalResource[] {}))
                    }));
        } catch (URISyntaxException e) {
            throw new ValidationException("URI is not valid!");
        }

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
        // Get current maDMP from InDMP
        final HttpEntity<String> request = new HttpEntity<>("", Functions.getHeaders(authorizedClient));
        final ResponseEntity<DMPScheme> reponse = Functions.sendHTTPRequest(log,
                indmpHost + indmpGetMaDMP
                        + Functions.getDMPParameters(testCaseEntity.getDmpScheme().getDmp(), new Date()),
                HttpMethod.GET, request, DMPScheme.class);

        // Are same?
        validateDMPScheme(testCaseEntity.getDmpScheme(), reponse.getBody());

        return Functions.processSuccess(log, "long maDMP was created");
    }

}
