package com.profile.searcher.service.mapper;

import com.profile.searcher.entity.PhantomAgentTaskEntity;
import com.profile.searcher.model.enums.PhantomAgentTaskStatus;
import com.profile.searcher.model.request.LinkedInProfileSearchDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = "spring",
        imports = {PhantomAgentTaskStatus.class})
public interface GenericModelMapper {

    @Mapping(target = "containerId", source = "containerId")
    @Mapping(target = "university", source = "linkedInProfileSearchDTO.university")
    @Mapping(target = "currentDesignation", source = "linkedInProfileSearchDTO.currentDesignation")
    @Mapping(target = "passedOutYear", source = "linkedInProfileSearchDTO.graduationYear")
    @Mapping(target = "phantomAgentTaskStatus", expression = "java(PhantomAgentTaskStatus.LAUNCHED)")
    PhantomAgentTaskEntity map(String containerId, LinkedInProfileSearchDTO linkedInProfileSearchDTO);
}
