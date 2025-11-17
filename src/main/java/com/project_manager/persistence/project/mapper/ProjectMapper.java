package com.project_manager.persistence.project.mapper;

import com.project_manager.business.project.model.Project;
import com.project_manager.persistence.project.entity.ProjectEntity;
import com.project_manager.shared.core.dateType.DateTypeMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = DateTypeMapper.class)
public interface ProjectMapper {
    Project toDomain(ProjectEntity entity);
    ProjectEntity toEntity(Project project);
    List<Project> toDomainList(List<ProjectEntity> entities);
    List<ProjectEntity> toEntityList(List<Project> projects);
}
