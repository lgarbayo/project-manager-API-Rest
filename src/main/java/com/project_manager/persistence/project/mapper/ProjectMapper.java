package com.project_manager.persistence.project.mapper;

import com.project_manager.business.project.model.Project;
<<<<<<< HEAD:src/main/java/com/project_manager/persistence/project/mapper/ProjectMapper.java
import com.project_manager.persistence.project.entity.ProjectEntity;
=======
import com.project_manager.business.project.sql.entity.ProjectEntity;
import com.project_manager.shared.core.dateType.DateTypeMapper;
import org.mapstruct.Mapper;
>>>>>>> 83c27c6 (feat: included mapstructs):src/main/java/com/project_manager/business/project/sql/mapper/ProjectMapper.java

import java.util.List;

@Mapper(componentModel = "spring", uses = DateTypeMapper.class)
public interface ProjectMapper {
    Project toDomain(ProjectEntity entity);
    ProjectEntity toEntity(Project project);
    List<Project> toDomainList(List<ProjectEntity> entities);
    List<ProjectEntity> toEntityList(List<Project> projects);
}
