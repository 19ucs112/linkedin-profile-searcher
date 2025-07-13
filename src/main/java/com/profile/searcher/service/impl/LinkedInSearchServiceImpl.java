package com.profile.searcher.service.impl;

import com.profile.searcher.model.request.LinkedInProfileSearchDTO;
import com.profile.searcher.model.response.SuccessResponseVO;
import com.profile.searcher.service.LinkedInSearchService;
import com.profile.searcher.service.PhantomBusterService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class LinkedInSearchServiceImpl implements LinkedInSearchService {

    private final PhantomBusterService phantomBusterService;


    @Override
    public SuccessResponseVO<UUID> searchAlumniLinkedInProfiles(LinkedInProfileSearchDTO linkedInProfileSearchDTO) {
        return phantomBusterService.searchLinkedInProfiles(linkedInProfileSearchDTO);
    }
}
