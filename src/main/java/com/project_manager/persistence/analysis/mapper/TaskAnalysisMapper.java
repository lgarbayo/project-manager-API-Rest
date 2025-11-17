package com.project_manager.persistence.analysis.mapper;

import com.project_manager.business.analysis.model.TaskAnalysis;
import com.project_manager.persistence.analysis.entity.TaskAnalysisEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskAnalysisMapper {

    TaskAnalysis toDomain(TaskAnalysisEntity entity);

    @Mapping(target = "milestoneAnalysis", ignore = true)
    TaskAnalysisEntity toEntity(TaskAnalysis analysis);

    List<TaskAnalysis> toDomainList(List<TaskAnalysisEntity> entities);
    List<TaskAnalysisEntity> toEntityList(List<TaskAnalysis> analyses);
}
