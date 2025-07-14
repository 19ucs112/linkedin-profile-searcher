package com.profile.searcher.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/***
 * Data Transfer Object (DTO) representing the input parameters required to initiate a LinkedIn profile search via
 * PhantomBuster. This object is typically used in API requests and validated before processing.
 */
@Data
public class LinkedInProfileSearchDTO {

    /***
     * Name of the university to search for alumni. This field is mandatory and must not be blank.
     */
    @NotBlank
    private String university;

    /***
     * Current job title or designation of the alumni to search. This field is mandatory and must not be blank.
     */
    @NotBlank
    private String currentDesignation;

    /***
     * Optional field indicating the graduation year of the alumni.
     */
    private Integer graduationYear;
}
