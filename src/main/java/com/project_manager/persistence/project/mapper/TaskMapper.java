package com.project_manager.persistence.project.mapper;

import com.project_manager.business.project.model.Task;
import com.project_manager.persistence.project.entity.TaskEntity;
import com.project_manager.shared.core.dateType.DateTypeMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = DateTypeMapper.class)
public interface TaskMapper {
    Task toDomain(TaskEntity entity);
    TaskEntity toEntity(Task task);
    List<Task> toDomainList(List<TaskEntity> entities);
    List<TaskEntity> toEntityList(List<Task> tasks);
}
