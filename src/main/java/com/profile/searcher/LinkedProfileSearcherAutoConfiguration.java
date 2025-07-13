package com.profile.searcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.profile.searcher.model.properties.PhantomBusterProperties;
import com.profile.searcher.repository.PhantomAgentTaskRepository;
import com.profile.searcher.repository.UniversityRepository;
import com.profile.searcher.service.LinkedInSearchService;
import com.profile.searcher.service.PhantomAgentTaskService;
import com.profile.searcher.service.PhantomBusterService;
import com.profile.searcher.service.client.PhantomBusterClient;
import com.profile.searcher.service.impl.LinkedInSearchServiceImpl;
import com.profile.searcher.service.impl.PhantomAgentTaskServiceImpl;
import com.profile.searcher.service.impl.PhantomBusterServiceImpl;
import com.profile.searcher.service.job.PhantomAgentTaskProcessingJob;
import com.profile.searcher.service.mapper.GenericModelMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@ComponentScan
@EnableScheduling
@EnableConfigurationProperties(PhantomBusterProperties.class)
public class LinkedProfileSearcherAutoConfiguration {

    @Bean
    public PhantomAgentTaskProcessingJob phantomAgentTaskProcessingJob(
            PhantomAgentTaskRepository phantomAgentTaskRepository, PhantomBusterClient phantomBusterClient,
            ObjectMapper objectMapper, GenericModelMapper genericModelMapper, UniversityRepository universityRepository) {
        return new PhantomAgentTaskProcessingJob(phantomAgentTaskRepository, phantomBusterClient, objectMapper,
                genericModelMapper, universityRepository);
    }

    @Bean
    public PhantomAgentTaskService phantomAgentTaskService(GenericModelMapper modelMapper,
                                                           PhantomAgentTaskRepository phantomAgentTaskRepository) {
        return new PhantomAgentTaskServiceImpl(modelMapper, phantomAgentTaskRepository);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(List.of(new MappingJackson2HttpMessageConverter()));
    }

    @Bean
    public PhantomBusterClient phantomBusterClient(RestTemplate restTemplate,
                                                   PhantomBusterProperties phantomBusterProperties,
                                                   ObjectMapper objectMapper) {
        return new PhantomBusterClient(restTemplate, phantomBusterProperties, objectMapper);
    }

    @Bean
    public PhantomBusterService phantomBusterService(PhantomBusterClient busterClient,
                                                     PhantomAgentTaskService phantomAgentTaskService) {
        return new PhantomBusterServiceImpl(busterClient, phantomAgentTaskService);
    }

    @Bean
    public LinkedInSearchService linkedInSearchService(PhantomBusterService phantomBusterService) {
        return new LinkedInSearchServiceImpl(phantomBusterService);
    }

    @Bean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.servers(List.of(new Server().url("http://localhost:8080")));
        return openAPI;
    }

}
