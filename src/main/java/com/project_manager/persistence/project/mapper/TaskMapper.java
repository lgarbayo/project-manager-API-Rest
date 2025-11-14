package com.project_manager.persistence.project.mapper;

import com.project_manager.business.project.model.Task;
<<<<<<< HEAD:src/main/java/com/project_manager/persistence/project/mapper/TaskMapper.java
import com.project_manager.persistence.project.entity.TaskEntity;
=======
import com.project_manager.business.project.sql.entity.TaskEntity;
import com.project_manager.shared.core.dateType.DateTypeMapper;
import org.mapstruct.Mapper;
>>>>>>> 83c27c6 (feat: included mapstructs):src/main/java/com/project_manager/business/project/sql/mapper/TaskMapper.java

import java.util.List;

@Mapper(componentModel = "spring", uses = DateTypeMapper.class)
public interface TaskMapper {
    Task toDomain(TaskEntity entity);
    TaskEntity toEntity(Task task);
    List<Task> toDomainList(List<TaskEntity> entities);
    List<TaskEntity> toEntityList(List<Task> tasks);
}
