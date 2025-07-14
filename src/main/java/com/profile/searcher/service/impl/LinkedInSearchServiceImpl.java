package com.profile.searcher.service.impl;

import com.profile.searcher.entity.PhantomAgentTaskEntity;
import com.profile.searcher.model.enums.PhantomAgentTaskStatus;
import com.profile.searcher.model.exceptions.LinkedInProfileSearcherException;
import com.profile.searcher.model.request.LinkedInProfileSearchDTO;
import com.profile.searcher.model.response.AlumniVO;
import com.profile.searcher.model.response.SuccessResponseVO;
import com.profile.searcher.repository.AlumniRepository;
import com.profile.searcher.service.LinkedInSearchService;
import com.profile.searcher.service.PhantomAgentTaskService;
import com.profile.searcher.service.PhantomBusterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/***
 * LinkedInSearchServiceImpl is the implementation of LinkedInSearchService interface.
 * Handles alumni profile searching logic using a hybrid approach of local database search and PhantomBuster-based
 * LinkedIn scraping if needed.
 */

@RequiredArgsConstructor
public class LinkedInSearchServiceImpl implements LinkedInSearchService {

    private final PhantomBusterService phantomBusterService;

    private final PhantomAgentTaskService phantomAgentTaskService;

    private final AlumniRepository alumniRepository;


    /***
     * Searches for alumni LinkedIn profiles based on the provided search criteria.
     * First, attempts to retrieve data from the local database. If no data is found, triggers a PhantomBuster
     * scraping process.
     * @param linkedInProfileSearchDTO the search parameters : designation, university, and graduation year.
     * @return successResponseVO containing a list of alumni or a tracking ID if scraping is triggered.
     */
    @Override
    public SuccessResponseVO<Object> searchAlumniLinkedInProfiles(LinkedInProfileSearchDTO linkedInProfileSearchDTO) {
        SuccessResponseVO<Object> successResponseVO = getAlumniData(linkedInProfileSearchDTO.getCurrentDesignation(),
                linkedInProfileSearchDTO.getUniversity(), linkedInProfileSearchDTO.getGraduationYear());
        List<AlumniVO> alumniVOList = (List<AlumniVO>) successResponseVO.getData();
        if (!alumniVOList.isEmpty()) {
            return successResponseVO;
        }
        return phantomBusterService.searchLinkedInProfiles(linkedInProfileSearchDTO);
    }

    /***
     * Retrieves the results of a previously launched PhantomBuster scraping task using a tracking ID.
     * @param trackingId the tracking identifier for the scraping task.
     * @return SuccessResponseVO with the scraped alumni data.
     * @throws LinkedInProfileSearcherException if the tracking ID is invalid or the task has failed.
     */
    @Override
    public SuccessResponseVO<Object> fetchScrapedAlumniLinkedInProfiles(UUID trackingId) {
        Optional<PhantomAgentTaskEntity> phantomAgentTask = phantomAgentTaskService
                .findPhantomAgentTaskByTrackingId(trackingId);
        if (phantomAgentTask.isEmpty()) {
            throw new LinkedInProfileSearcherException("Invalid trackingId");
        }
        PhantomAgentTaskEntity phantomAgentTaskEntity = phantomAgentTask.get();
        if (PhantomAgentTaskStatus.AGENT_LAUNCHED.equals(phantomAgentTaskEntity.getPhantomAgentTaskStatus())) {
            return SuccessResponseVO.of("LinkedIn profile scraping is still in progress", null);
        }
        if (PhantomAgentTaskStatus.TASK_FAILED.equals(phantomAgentTaskEntity.getPhantomAgentTaskStatus())) {
            throw new LinkedInProfileSearcherException("LinkedIn profile scraping failed");
        }
        return getAlumniData(phantomAgentTaskEntity.getCurrentDesignation(), phantomAgentTaskEntity.getUniversity(),
                phantomAgentTaskEntity.getPassedOutYear());
    }

    /***
     * Fetches all alumni profiles from the database with pagination support.
     * @param page the page number.
     * @param limit the number of records per page.
     * @return SuccessResponseVO containing paginated list of alumni records.
     */
    @Override
    public SuccessResponseVO<List<AlumniVO>> fetchAllAlumni(int page, int limit) {
        if (page < 0) {
            page = 0;
        }
        if (limit <= 0) {
            limit = 10;
        }
        Pageable pageable = PageRequest.of(page, limit);
        return SuccessResponseVO.of("Fetched all alumni successfully",
                alumniRepository.findAllAlumni(pageable).stream().toList());
    }

    private SuccessResponseVO<Object> getAlumniData(String currentDesignation, String university,
                                                    Integer graduationYear) {
        List<AlumniVO> alumniVOList;
        if (graduationYear != null) {
            alumniVOList = alumniRepository.findAllAlumni(currentDesignation, university, graduationYear);
        } else {
            alumniVOList = alumniRepository.findAllAlumniWithoutGraduationYear(currentDesignation, university);
        }
        return SuccessResponseVO.of("Fetched alumni data successfully", alumniVOList);
    }
}
