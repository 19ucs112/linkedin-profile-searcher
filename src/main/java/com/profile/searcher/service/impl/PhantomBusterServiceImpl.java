package com.profile.searcher.service.impl;

import com.profile.searcher.entity.PhantomAgentTaskEntity;
import com.profile.searcher.model.request.LinkedInProfileSearchDTO;
import com.profile.searcher.model.request.phantom.buster.PhantomLaunchResponse;
import com.profile.searcher.model.response.SuccessResponseVO;
import com.profile.searcher.repository.PhantomAgentTaskRepository;
import com.profile.searcher.service.PhantomBusterService;
import com.profile.searcher.service.client.PhantomBusterClient;
import com.profile.searcher.service.mapper.GenericModelMapper;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class PhantomBusterServiceImpl implements PhantomBusterService {

    private final PhantomBusterClient phantomBusterClient;

    private final GenericModelMapper mapper;

    private final PhantomAgentTaskRepository phantomAgentTaskRepository;

    @Override
    public SuccessResponseVO<UUID> searchLinkedInProfiles(LinkedInProfileSearchDTO linkedInProfileSearchDTO) {
        PhantomLaunchResponse phantomLaunchResponse = phantomBusterClient
                .triggerPhantomBusterSearch(linkedInProfileSearchDTO.getCurrentDesignation(),
                        linkedInProfileSearchDTO.getUniversity());
        UUID trackingId = savePhantomAgentTask(phantomLaunchResponse, linkedInProfileSearchDTO);
        return SuccessResponseVO.of("Phantom to scrap linkedIn data launched successfully. " +
                "You can access your data using result api by passing given tracking id with in few minutes", trackingId);
    }

    private UUID savePhantomAgentTask(PhantomLaunchResponse phantomLaunchResponse,
                                      LinkedInProfileSearchDTO linkedInProfileSearchDTO) {
        PhantomAgentTaskEntity entity = mapper.map(phantomLaunchResponse.getContainerId(),
                linkedInProfileSearchDTO);
        return  phantomAgentTaskRepository.save(entity).getId();
    }
}
