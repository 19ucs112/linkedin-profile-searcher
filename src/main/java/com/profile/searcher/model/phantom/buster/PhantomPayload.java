package com.profile.searcher.model.phantom.buster;

import lombok.Getter;
import lombok.Setter;

/***
 * Represents the payload structure used to trigger a PhantomBuster agent execution. This payload contains the agent ID,
 * output preference, and the input arguments required by the agent to perform a LinkedIn profile search.
 */
@Getter
@Setter
public class PhantomPayload {

    /***
     * The unique identifier of the PhantomBuster agent to be triggered.
     */
    private String id;

    /***
     * Specifies the output format. By default, it returns the first result object.
     */
    private String output = "first-result-object";

    /***
     * The input parameters required by the PhantomBuster agent to perform the scraping task.
     */
    private PhantomBusterInput argument;

    /***
     * Represents the specific input values passed to the PhantomBuster agent. These values control how the LinkedIn
     * search is conducted, including search type, session authentication, and result limits.
     */
    @Getter
    @Setter
    public static class PhantomBusterInput {

        /***
         * The LinkedIn search URL to be used by the agent.
         */
        private String search;

        /***
         * The keywords used for searching LinkedIn profiles (e.g., job title, university).
         */
        private String keywords;

        /***
         * The LinkedIn session cookie used for authenticated access.
         */
        private String sessionCookie;

        /***
         * Maximum number of results to return per launch. Default is 10.
         */
        private int numberOfResultsPerLaunch = 10;

        /***
         * Maximum number of results to return per search. Default is 10.
         */
        private int numberOfResultsPerSearch = 10;

        /***
         * Type of search to be performed. Default is "keywords".
         */
        private String searchType = "keywords";
    }
}

