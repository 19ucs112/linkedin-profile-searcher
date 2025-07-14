package com.profile.searcher.model.enums;

/***
 * Enum representing the status of a PhantomBuster agent task. These statuses indicate the current state of a scraping
 * task initiated to fetch LinkedIn profile data.
 */
public enum PhantomAgentTaskStatus {
    AGENT_LAUNCHED,
    DATA_RECEIVED,
    TASK_FAILED;
}
