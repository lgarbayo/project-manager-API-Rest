package com.project_manager.persistence.analysis.mapper;

import com.project_manager.business.analysis.model.ProjectAnalysis;
<<<<<<< HEAD:src/main/java/com/project_manager/persistence/analysis/mapper/ProjectAnalysisMapper.java
import com.project_manager.business.analysis.model.MilestoneAnalysis;
import com.project_manager.business.analysis.model.TaskAnalysis;
import com.project_manager.persistence.analysis.entity.MilestoneAnalysisEntity;
import com.project_manager.persistence.analysis.entity.ProjectAnalysisEntity;
import com.project_manager.persistence.analysis.entity.TaskAnalysisEntity;
=======
import com.project_manager.business.analysis.sql.entity.ProjectAnalysisEntity;
import com.project_manager.shared.core.dateType.DateTypeMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
>>>>>>> 83c27c6 (feat: included mapstructs):src/main/java/com/project_manager/business/analysis/sql/mapper/ProjectAnalysisMapper.java

import java.util.List;

@Mapper(componentModel = "spring", uses = {DateTypeMapper.class, MilestoneAnalysisMapper.class})
public interface ProjectAnalysisMapper {

    @Mapping(target = "project.uuid", source = "projectUuid")
    @Mapping(target = "project.title", source = "projectTitle")
    @Mapping(target = "project.description", source = "projectDescription")
    @Mapping(target = "project.startDate", source = "startDate")
    @Mapping(target = "project.endDate", source = "endDate")
    @Mapping(target = "project.additionalFields", source = "additionalFields")
    @Mapping(target = "milestoneList", source = "milestoneAnalyses")
    ProjectAnalysis toDomain(ProjectAnalysisEntity entity);

    @InheritInverseConfiguration
    ProjectAnalysisEntity toEntity(ProjectAnalysis analysis);

    List<ProjectAnalysis> toDomainList(List<ProjectAnalysisEntity> entities);
    List<ProjectAnalysisEntity> toEntityList(List<ProjectAnalysis> analyses);

    // to set bidirectional relationships: each MilestoneAnalysisEntity must reference its parent ProjectAnalysisEntity
    @AfterMapping
    default void linkMilestones(@MappingTarget ProjectAnalysisEntity entity) {
        if (entity.getMilestoneAnalyses() != null) {
            entity.getMilestoneAnalyses().forEach(milestone -> milestone.setProjectAnalysis(entity));
        }
    }
}
