package com.profile.searcher.service;

import com.profile.searcher.model.request.LinkedInProfileSearchDTO;

import java.util.UUID;

public interface PhantomAgentTaskService {

    UUID createPhantomBulkConsentTask(String containerId, LinkedInProfileSearchDTO linkedInProfileSearchDTO);
}
