package com.profile.searcher.rest.v1;

import com.profile.searcher.model.request.LinkedInProfileSearchDTO;
import com.profile.searcher.model.response.AlumniVO;
import com.profile.searcher.model.response.SuccessResponseVO;
import com.profile.searcher.service.LinkedInSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


/***
 * LinkedlnProfileController is used for handling LinkedIn profile search and retrieval operation.
 * This controller have three endpoints. 1st is for searching alumni profiles on LinkedIn based on input criteria.
 * 2nd one is for fetching scraped alumni profiles based on the tracking id.
 * 3rd one is for retrieving all alumni profiles with pagination.
 */

@RestController
@RequestMapping("/api/v1/linked-in")
@RequiredArgsConstructor
@Validated
public class LinkedInProfileController {

    /***
     * Service layer dependency for LinkedIn profile search and retrieval operations.
     */

    private final LinkedInSearchService linkedInSearchService;

    /***
     * Initiates the LinkedIn alumni profile based on the specified criteria.
     * @param linkedInProfileSearchDTO the search criteria
     * @return ResponseEntity with the search result or tracking info.
     */

    @PostMapping("/search")
    public ResponseEntity<SuccessResponseVO<Object>> searchAlumniLinkedInProfiles(
            @RequestBody @Valid LinkedInProfileSearchDTO linkedInProfileSearchDTO) {
        return ResponseEntity.ok(linkedInSearchService.searchAlumniLinkedInProfiles(linkedInProfileSearchDTO));
    }

    /***
     * Fetches the results of a previously initiated alumni profile search using the tracking ID.
     *
     * @param trackingId the UUID used to identify the scraping task.
     * @return a ResponseEntity containing a {SuccessResponseVO} with the list of scraped profiles
     */
    @GetMapping("/fetch/{trackingId}")
    public ResponseEntity<SuccessResponseVO<Object>> fetchScrapedAlumniProfiles(@PathVariable UUID trackingId) {
        return ResponseEntity.ok(linkedInSearchService.fetchScrapedAlumniLinkedInProfiles(trackingId));
    }

    /***
     * Retrieves all stored linkedin alumni profiles using pagination support.
     * @param page the page number to retrieve.
     * @param limit the number of records per page.
     * @return a ResponseEntity containing a SuccessResponseVO with a list of AlumniVO objects.
     */
    @GetMapping("/fetch/all")
    public ResponseEntity<SuccessResponseVO<List<AlumniVO>>> fetchAllAlumni(@RequestParam int page,
                                                                            @RequestParam int limit) {
        return ResponseEntity.ok(linkedInSearchService.fetchAllAlumni(page, limit));
    }
}
