package com.profile.searcher.service.client;

import com.profile.searcher.model.properties.PhantomBusterProperties;
import com.profile.searcher.model.request.phantom.buster.PhantomLaunchResponse;
import com.profile.searcher.model.request.phantom.buster.PhantomPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;

@RequiredArgsConstructor
@Slf4j
public class PhantomBusterClient {

    private static final String PHANTOM_URL = "https://api.phantombuster.com/api/v2/agents/launch";
    private static final String LINKEDIN_SEARCH_URL = "https://www.linkedin.com/search/results/people/";

    private final RestTemplate restTemplate;

    private final PhantomBusterProperties properties;


    public PhantomLaunchResponse triggerPhantomBusterSearch(String currentDesignation, String university) {
        PhantomPayload payload = createPhantomBusterPayload(currentDesignation, university);
        HttpHeaders headers = creatHttpHeaders();
        HttpEntity<PhantomPayload> httpEntity = new HttpEntity<>(payload, headers);
        try {
            PhantomLaunchResponse response = restTemplate.postForObject(PHANTOM_URL, httpEntity,
                    PhantomLaunchResponse.class);
            return response;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
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
