package at.tuwien.dmp.util;

import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class Functions {

    private Functions() {
        throw new AssertionError();
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
     * Send http request
     * 
     * @param endpoint
     * @param httpMethod
     * @param request
     * @return
     * @throws HttpClientErrorException
     */
    public static ResponseEntity<String> sendHTTPRequest(String endpoint, HttpMethod httpMethod, HttpEntity<?> request)
            throws HttpClientErrorException {
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(endpoint, httpMethod, request, String.class);
    }

    /**
     * 
     * Process response message
     * 
     * @param success
     * @param responseEntity
     * @param e
     * @return
     */
    public static String processResponse(Logger log, ResponseEntity<?> responseEntity) {
        if (responseEntity != null) {
            // Generate response message
            String response = "Success:\nStatus code: " + responseEntity.getStatusCode();
            // Add body
            if (responseEntity.getBody() != null) {
                response += "\nBody: " + responseEntity.getBody();
            }
            log.info(response);
            return response;
        } else {
            log.info("Success: Unknown body");
            return "Success: Unknown body";
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
    public static String processError(Logger log, HttpClientErrorException e) {
        if (e != null) {
            log.error("Failed: " + e.getMessage());
            return "Failed: " + e.getMessage();
        } else {
            log.error("Failed: Unknown error");
            return "Failed: Unknown error";
        }
    }
}
