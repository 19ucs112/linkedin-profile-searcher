package com.profile.searcher.service.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.profile.searcher.entity.AlumniEntity;
import com.profile.searcher.entity.PhantomAgentTaskEntity;
import com.profile.searcher.entity.UniversityEntity;
import com.profile.searcher.model.enums.PhantomAgentTaskStatus;
import com.profile.searcher.model.phantom.buster.LinkedInProfileExportAgentResponse;
import com.profile.searcher.model.phantom.buster.LinkedInProfileScrapResponse;
import com.profile.searcher.repository.PhantomAgentTaskRepository;
import com.profile.searcher.repository.UniversityRepository;
import com.profile.searcher.service.client.PhantomBusterClient;
import com.profile.searcher.service.mapper.GenericModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class PhantomAgentTaskProcessingJob {

    private final PhantomAgentTaskRepository phantomAgentTaskRepository;
    private final PhantomBusterClient phantomBusterClient;
    private final ObjectMapper objectMapper;
    private final GenericModelMapper genericModelMapper;
    private final UniversityRepository universityRepository;

    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.MINUTES)
    public void processPhantomAgentTask() {
        List<PhantomAgentTaskEntity> phantomAgentTaskEntities = phantomAgentTaskRepository
                .findAllByPhantomAgentTaskStatus(PhantomAgentTaskStatus.AGENT_LAUNCHED);
        phantomAgentTaskEntities.forEach(phantomAgentTaskEntity -> {
            LinkedInProfileScrapResponse response = phantomBusterClient
                    .getContainerOutput(phantomAgentTaskEntity.getContainerId());
            if (response != null && "finished".equals(response.getStatus())) {
                List<LinkedInProfileExportAgentResponse> agentResponse;
                try {
                    agentResponse = objectMapper.readValue(response.getResultObject(),
                            new TypeReference<>() {
                            });
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                UniversityEntity university = getUniversityEntity(phantomAgentTaskEntity.getUniversity());
                List<AlumniEntity> alumniEntities = createAlumni(university, agentResponse);
                university.setAlumniEntities(alumniEntities);
                universityRepository.save(university);
                phantomAgentTaskEntity.setPhantomAgentTaskStatus(PhantomAgentTaskStatus.DATA_RECEIVED);
            } else {
                if ("finished".equals(response.getStatus()) && response.getExitCode() == 1) {
                    phantomAgentTaskEntity.setPhantomAgentTaskStatus(PhantomAgentTaskStatus.TASK_FAILED);
                } else {
                    if (phantomAgentTaskEntity.getRetryCount() < 1) {
                        phantomAgentTaskEntity.setRetryCount(phantomAgentTaskEntity.getRetryCount() + 1);
                    }
                    phantomAgentTaskEntity.setPhantomAgentTaskStatus(PhantomAgentTaskStatus.TASK_FAILED);
                }
            }
        });
        phantomAgentTaskRepository.saveAll(phantomAgentTaskEntities);
    }

    private UniversityEntity getUniversityEntity(String university) {
        UniversityEntity universityEntity = new UniversityEntity();
        universityEntity.setName(university);
        return universityEntity;
    }

    private List<AlumniEntity> createAlumni(UniversityEntity university,
                                            List<LinkedInProfileExportAgentResponse> agentResponse) {
        List<AlumniEntity> alumniEntities = new ArrayList<>();
        agentResponse.forEach(agentResponseVO ->
                alumniEntities.add(genericModelMapper.map(agentResponseVO, university.getName())));
        return alumniEntities;
    }
}
