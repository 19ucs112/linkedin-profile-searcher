package com.profile.searcher.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.profile.searcher.model.exceptions.LinkedInProfileSearcherException;
import com.profile.searcher.model.phantom.buster.LinkedInProfileScrapResponse;
import com.profile.searcher.model.phantom.buster.PhantomLaunchResponse;
import com.profile.searcher.model.phantom.buster.PhantomPayload;
import com.profile.searcher.model.properties.PhantomBusterProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;

/***
 * Client class is responsible for interacting with PhantomBuster's API.
 * This client allows triggering LinkedIn searches via PhantomBuster and retrieving the scraped results based on
 * container IDs.
 */

@RequiredArgsConstructor
@Slf4j
public class PhantomBusterClient {

    /***
     * URL to trigger the PhantomBuster agent.
     */
    private static final String PHANTOM_URL = "https://api.phantombuster.com/api/v2/agents/launch";

    /***
     * Base URL for LinkedIn people search.
     */
    private static final String LINKEDIN_SEARCH_URL = "https://www.linkedin.com/search/results/people/";

    /***
     * URL to fetch the container output after PhantomBuster execution.
     */
    private static final String PHANTOM_CONTAINER_OUTPUT_URL = "https://api.phantombuster.com/api/v2/containers/fetch";

    private final RestTemplate restTemplate;

    private final PhantomBusterProperties properties;

    private final ObjectMapper objectMapper;

    /***
     * Triggers a LinkedIn profile search using PhantomBuster with the given designation and university.
     * @param currentDesignation the current job title or designation to search.
     * @param university the university name to include in the search.
     * @return PhantomPayload containing execution metadata and container ID
     */
    public PhantomLaunchResponse triggerPhantomBusterSearch(String currentDesignation, String university) {
        PhantomPayload payload = createPhantomBusterPayload(currentDesignation, university);
        HttpHeaders headers = creatHttpHeaders();
        HttpEntity<PhantomPayload> httpEntity = new HttpEntity<>(payload, headers);
        try {
            return restTemplate.postForObject(PHANTOM_URL, httpEntity,
                    PhantomLaunchResponse.class);
        } catch (Exception ex) {
            throw new LinkedInProfileSearcherException(ex.getMessage());
        }
    }

    /***
     * Fetches the output (scraped data) from PhantomBuster using the container ID.
     * @param containerId The unique identifier for teh PhantomBuster container.
     * @return LinkedInProfileScrapResponse containing the scraped LinkedIn profiles data.
     */
    public LinkedInProfileScrapResponse getContainerOutput(String containerId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(PHANTOM_CONTAINER_OUTPUT_URL)
                .queryParam("id", containerId)
                .queryParam("withResultObject", "true");
        HttpHeaders headers = creatHttpHeaders();
        HttpEntity httpEntity = new HttpEntity(headers);
        try {
            ResponseEntity<LinkedInProfileScrapResponse> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, httpEntity,
                    LinkedInProfileScrapResponse.class);
            return objectMapper.convertValue(response.getBody(),
                    LinkedInProfileScrapResponse.class);
        } catch (Exception ex) {
            throw new LinkedInProfileSearcherException(ex.getMessage());
        }
    }

    private PhantomPayload createPhantomBusterPayload(String currentDesignation, String university) {
        PhantomPayload payload = new PhantomPayload();
        PhantomPayload.PhantomBusterInput busterInput = new PhantomPayload.PhantomBusterInput();
        busterInput.setKeywords(currentDesignation + ", " + university);
        busterInput.setSessionCookie(properties.getLinkedInSessionCookie());
        busterInput.setSearch(getLinkedInSearchUrl(currentDesignation));
        payload.setId(properties.getPhantomId());
        payload.setArgument(busterInput);
        return payload;
    }

    private String getLinkedInSearchUrl(String currentDesignation) {
        return UriComponentsBuilder.fromHttpUrl(LINKEDIN_SEARCH_URL).queryParam("keywords",
                URLEncoder.encode(currentDesignation)).build().toUriString();
    }

    private HttpHeaders creatHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Phantombuster-Key-1", properties.getPhantomBusterApiKey());
        httpHeaders.add("content-type", MediaType.APPLICATION_JSON_VALUE);
        return httpHeaders;
    }
}
