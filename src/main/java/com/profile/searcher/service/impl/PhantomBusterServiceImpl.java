package com.profile.searcher.service.impl;

import com.profile.searcher.model.phantom.buster.PhantomLaunchResponse;
import com.profile.searcher.model.request.LinkedInProfileSearchDTO;
import com.profile.searcher.model.response.SuccessResponseVO;
import com.profile.searcher.service.PhantomAgentTaskService;
import com.profile.searcher.service.PhantomBusterService;
import com.profile.searcher.service.client.PhantomBusterClient;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/***
 * PhantomBusterServiceImpl class is the implementation of PhantomBusterService interface.
 * This service is responsible for triggering PhantomBuster to perform a LinkedIn search, storing metadata for tracking
 * the search task, and returning a tracking ID to the client.
 */
@RequiredArgsConstructor
public class PhantomBusterServiceImpl implements PhantomBusterService {

    /***
     * phantomBusterClient class is autowired to interact with PhantomBuster's API.
     */
    private final PhantomBusterClient phantomBusterClient;

    /***
     * phantomAgentTaskService class is autowired to persist and manage PhantomBuster task tracking information.
     */
    private final PhantomAgentTaskService phantomAgentTaskService;

    /***
     * Launches a PhantomBuster scraping task for LinkedIn profiles based on the provided search criteria. It sends a
     * request to PhantomBuster to start the agent, and stores the task metadata using the generated container ID. A
     * tracking ID is returned so the client can later retrieve the results.
     * @param linkedInProfileSearchDTO the search criteria including current designation and university.
     * @return SuccessResponseVO containing a message and a tracking ID for retrieving results later.
     */
    @Override
    public SuccessResponseVO<Object> searchLinkedInProfiles(LinkedInProfileSearchDTO linkedInProfileSearchDTO) {
        PhantomLaunchResponse phantomLaunchResponse = phantomBusterClient
                .triggerPhantomBusterSearch(linkedInProfileSearchDTO.getCurrentDesignation(),
                        linkedInProfileSearchDTO.getUniversity());
        UUID trackingId = phantomAgentTaskService.createPhantomBulkConsentTask(phantomLaunchResponse.getContainerId(),
                linkedInProfileSearchDTO);
        return SuccessResponseVO.of("Phantom to scrap linkedIn data launched successfully. " +
                "You can access your data using result api by passing given tracking id with in few minutes", trackingId);
    }
}
