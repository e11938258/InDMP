package at.tuwien.simulation.util;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import at.tuwien.simulation.model.Entity;
import at.tuwien.simulation.model.dmp.DMP;

public class Functions {

    private Functions() {
        throw new AssertionError();
    }

    /**
     * 
     * Find property in the second list with entities
     * 
     * @param property
     * @param properties2
     * @return
     */
    public static void validateProperty(Entity property, List<Entity> properties2) {
        final List<Entity> results = properties2.stream()
                .filter(p -> p.getAtLocation().equals(property.getAtLocation()))
                .filter(p -> p.getSpecializationOf().equals(property.getSpecializationOf()))
                .filter(p -> p.getValue().equals(property.getValue()))
                .collect(Collectors.toList());
        if (results.size() != 1) {
            throw new ValidationException("Cannot find entity: " + property.toString());
        }
    }

    /**
     * 
     * Create a new entity
     * 
     * @param location
     * @param propertyName
     * @param value
     * @return
     */
    public static Entity createEntity(String location, String propertyName, String value) {
        // Add a new entity
        final Entity entity = new Entity(location, propertyName, value);
        return entity;
    }

    /**
     * 
     * Get headers for http request to indmp service
     * 
     * @param authorizedClient
     * @return
     */
    public static HttpHeaders getHeaders(OAuth2AuthorizedClient authorizedClient) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue());
        return headers;
    }

    /**
     * 
     * Get DMP parameters
     * 
     * @return
     */
    public static String getDMPParameters(DMP dmp, Date modified) {
        return "?identifier=" + dmp.getClassIdentifier() + "&created="
                + ModelConstants.DATE_TIME_FORMATTER_ISO_8601.format(dmp.getCreated()) + "&modified="
                + ModelConstants.DATE_TIME_FORMATTER_ISO_8601.format(modified);
    }

    /**
     * 
     * Send http request
     * 
     * @param endpoint
     * @param httpMethod
     * @param request
     * @return
     * @throws HttpClientErrorException
     */
    public static <T> ResponseEntity<T> sendHTTPRequest(Logger log, String endpoint, HttpMethod httpMethod,
            HttpEntity<?> request, Class<T> clazz)
            throws HttpClientErrorException {
        final RestTemplate restTemplate = new RestTemplate();
        log.info("Sending a http request " + httpMethod + " to " + endpoint + ".");
        return restTemplate.exchange(endpoint, httpMethod, request, clazz);
    }

    /**
     * 
     * Process message
     * 
     * @param success
     * @param responseEntity
     * @param e
     * @return
     */
    public static String processSuccess(Logger log, String message) {
        if (message != null) {
            String response = "Success! Message: " + message;
            log.info(response);
            return response;
        } else {
            log.info("Success: Unknown message");
            return "Success: Unknown message";
        }
    }

    /**
     * 
     * Process error message
     * 
     * @param success
     * @param responseEntity
     * @param e
     * @return
     */
    public static String processError(Logger log, String message) {
        if (message != null) {
            log.error("Failed! Message: " + message);
            return "Failed! Message: " + message;
        } else {
            log.error("Failed! Unknown error");
            return "Failed! Unknown error";
        }
    }

    /**
     *
     * Generate a random string
     *
     * @param length of string
     * @param string
     * @return a random string
     */
    public static String generateRandomString(int length, final String string) {
        StringBuilder builder = new StringBuilder();

        while (length-- != 0) {
            int character = (int) (Math.random() * string.length());
            builder.append(string.charAt(character));
        }

        return builder.toString();
    }

    /**
     *
     * Generate a random number between start and end
     *
     * @param min starting integer
     * @param max ending integer - not included
     * @return integer
     */
    public static int getRandomNumberBetween(int min, int max) {
        return min + (int) Math.round(Math.random() * (max - min));
    }
}
