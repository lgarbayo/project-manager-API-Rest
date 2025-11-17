package com.project_manager.persistence.project.mapper;

import com.project_manager.shared.core.dateType.DateType;
import com.project_manager.business.project.model.Task;
import com.project_manager.persistence.project.entity.TaskEntity;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    public Task toDomain(TaskEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Task task = new Task();
        task.setUuid(entity.getUuid());
        task.setProjectUuid(entity.getProjectUuid());
        task.setTitle(entity.getTitle());
        task.setDescription(entity.getDescription());
        task.setDurationWeeks(entity.getDurationWeeks());
        task.setStartDate(localDateToDateType(entity.getStartDate()));
        
        return task;
    }

    public TaskEntity toEntity(Task task) {
        if (task == null) {
            return null;
        }
        
        TaskEntity entity = new TaskEntity();
        entity.setUuid(task.getUuid());
        entity.setProjectUuid(task.getProjectUuid());
        entity.setTitle(task.getTitle());
        entity.setDescription(task.getDescription());
        entity.setDurationWeeks(task.getDurationWeeks());
        entity.setStartDate(dateTypeToLocalDate(task.getStartDate()));
        
        return entity;
    }

    public List<Task> toDomainList(List<TaskEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public List<TaskEntity> toEntityList(List<Task> tasks) {
        return tasks.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    private DateType localDateToDateType(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        
        DateType dateType = new DateType();
        dateType.setYear(localDate.getYear());
        dateType.setMonth(localDate.getMonthValue() - 1);
        
        int dayOfMonth = localDate.getDayOfMonth();
        int week = (dayOfMonth - 1) / 7;
        dateType.setWeek(Math.min(week, 3));
        
        return dateType;
    }

    private LocalDate dateTypeToLocalDate(DateType dateType) {
        if (dateType == null) {
            return null;
        }
        
        int month = dateType.getMonth() + 1;
        int dayOfMonth = (dateType.getWeek() * 7) + 1;
        
        int year = dateType.getYear();
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        int maxDayOfMonth = firstDayOfMonth.lengthOfMonth();
        dayOfMonth = Math.min(dayOfMonth, maxDayOfMonth);
        
        return LocalDate.of(year, month, dayOfMonth);
    }
}