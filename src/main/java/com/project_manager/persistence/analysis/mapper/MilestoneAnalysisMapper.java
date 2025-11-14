package com.project_manager.business.analysis.sql.mapper;

import com.project_manager.business.analysis.model.MilestoneAnalysis;
import com.project_manager.business.analysis.sql.entity.MilestoneAnalysisEntity;
import com.project_manager.shared.core.dateType.DateTypeMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DateTypeMapper.class, TaskAnalysisMapper.class})
public interface MilestoneAnalysisMapper {

    @Mapping(target = "taskList", source = "taskAnalyses")
    MilestoneAnalysis toDomain(MilestoneAnalysisEntity entity);

    @Mapping(target = "taskAnalyses", source = "taskList")
    @Mapping(target = "projectAnalysis", ignore = true)
    MilestoneAnalysisEntity toEntity(MilestoneAnalysis analysis);

    List<MilestoneAnalysis> toDomainList(List<MilestoneAnalysisEntity> entities);
    List<MilestoneAnalysisEntity> toEntityList(List<MilestoneAnalysis> analyses);

    // to set bidirectional relationships: each TaskAnalysisEntity must reference its parent MilestoneAnalysisEntity
    @AfterMapping
    default void linkTasks(@MappingTarget MilestoneAnalysisEntity entity) {
        if (entity.getTaskAnalyses() != null) {
            entity.getTaskAnalyses().forEach(task -> task.setMilestoneAnalysis(entity));
        }
    }
}
