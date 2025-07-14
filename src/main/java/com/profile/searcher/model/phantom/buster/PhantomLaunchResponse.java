package com.profile.searcher.model.phantom.buster;

import lombok.Getter;
import lombok.Setter;

/***
 * Represents the response received after launching a PhantomBuster agent. This response contains the
 * unique container ID, which is used to track the execution and retrieve results later.
 */
@Getter
@Setter
public class PhantomLaunchResponse {

    /***
     * The containerId unique identifier for the PhantomBuster is used to query the status and results of the scraping task.
     */
    private String containerId;
}
