package com.profile.searcher.service.impl;

import com.profile.searcher.entity.PhantomAgentTaskEntity;
import com.profile.searcher.model.request.LinkedInProfileSearchDTO;
import com.profile.searcher.repository.PhantomAgentTaskRepository;
import com.profile.searcher.service.PhantomAgentTaskService;
import com.profile.searcher.service.mapper.GenericModelMapper;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

/***
 * PhantomAgentTaskServiceImpl is the implementation of PhantomAgentTaskService interface.
 * This service manages persistence and retrieval of PhantomBuster agent task metadata.
 */

@RequiredArgsConstructor
public class PhantomAgentTaskServiceImpl implements PhantomAgentTaskService {

    private final GenericModelMapper modelMapper;

    private final PhantomAgentTaskRepository taskRepository;

    /***
     * Creates and stores a new PhantomBuster agent task using the given container ID and search criteria.
     * @param containerId the ID returned by PhantomBuster after launching the agent.
     * @param linkedInProfileSearchDTO the search criteria : designation, university, graduation year.
     * @return UUID for the newly saved task entity.
     */
    @Override
    public UUID createPhantomBulkConsentTask(String containerId, LinkedInProfileSearchDTO linkedInProfileSearchDTO) {
        PhantomAgentTaskEntity entity = modelMapper.map(containerId, linkedInProfileSearchDTO);
        return taskRepository.save(entity).getId();
    }

    /***
     * Finds a PhantomBuster agent task by its unique tracking ID.
     * @param trackingId the UUID used to identify the task.
     * @return Optional PhantomAgentTaskEntity containing the task entity if found, otherwise empty.
     */
    @Override
    public Optional<PhantomAgentTaskEntity> findPhantomAgentTaskByTrackingId(UUID trackingId) {
        return taskRepository.findById(trackingId);
    }
}
