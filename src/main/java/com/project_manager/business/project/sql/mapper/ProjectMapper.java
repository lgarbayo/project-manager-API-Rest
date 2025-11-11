package com.project_manager.business.project.sql.mapper;

import com.project_manager.shared.core.dateType.DateType;
import com.project_manager.business.project.model.Project;
import com.project_manager.business.project.sql.entity.ProjectEntity;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    public Project toDomain(ProjectEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Project project = new Project();
        project.setUuid(entity.getUuid());
        project.setTitle(entity.getTitle());
        project.setDescription(entity.getDescription());
        project.setStartDate(localDateToDateType(entity.getStartDate()));
        project.setEndDate(localDateToDateType(entity.getEndDate()));
        project.setAdditionalFields(entity.getAdditionalFields());
        
        return project;
    }

    public ProjectEntity toEntity(Project project) {
        if (project == null) {
            return null;
        }
        
        ProjectEntity entity = new ProjectEntity();
        entity.setUuid(project.getUuid());
        entity.setTitle(project.getTitle());
        entity.setDescription(project.getDescription());
        entity.setStartDate(dateTypeToLocalDate(project.getStartDate()));
        entity.setEndDate(dateTypeToLocalDate(project.getEndDate()));
        entity.setAdditionalFields(project.getAdditionalFields());
        
        return entity;
    }

    public List<Project> toDomainList(List<ProjectEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public List<ProjectEntity> toEntityList(List<Project> projects) {
        return projects.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    private DateType localDateToDateType(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        
        DateType dateType = new DateType();
        dateType.setYear(localDate.getYear());
        dateType.setMonth(localDate.getMonthValue() - 1); // LocalDate uses 1-12, DateType uses 0-11
        
        // Calculate week of month (approximate)
        int dayOfMonth = localDate.getDayOfMonth();
        int week = (dayOfMonth - 1) / 7; // 0-based week of month
        dateType.setWeek(Math.min(week, 3)); // DateType uses 0-3
        
        return dateType;
    }

    private LocalDate dateTypeToLocalDate(DateType dateType) {
        if (dateType == null) {
            return null;
        }
        
        int month = dateType.getMonth() + 1; // DateType uses 0-11, LocalDate uses 1-12
        int dayOfMonth = (dateType.getWeek() * 7) + 1; // Approximate day from week
        
        // Ensure valid day of month
        int year = dateType.getYear();
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        int maxDayOfMonth = firstDayOfMonth.lengthOfMonth();
        dayOfMonth = Math.min(dayOfMonth, maxDayOfMonth);
        
        return LocalDate.of(year, month, dayOfMonth);
    }
}
