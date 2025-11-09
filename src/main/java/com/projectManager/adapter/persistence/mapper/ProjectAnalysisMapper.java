package com.projectManager.adapter.persistence.mapper;

import com.projectManager.adapter.persistence.entity.ProjectAnalysisEntity;
import com.projectManager.adapter.persistence.entity.MilestoneAnalysisEntity;
import com.projectManager.adapter.persistence.entity.TaskAnalysisEntity;
import com.projectManager.core.dateType.DateType;
import com.projectManager.core.project.ProjectCoreData;
import com.projectManager.domain.analysis.ProjectAnalysis;
import com.projectManager.domain.analysis.MilestoneAnalysis;
import com.projectManager.domain.analysis.TaskAnalysis;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectAnalysisMapper {

    public ProjectAnalysis toDomain(ProjectAnalysisEntity entity) {
        if (entity == null) {
            return null;
        }
        
        ProjectCoreData projectCoreData = new ProjectCoreData();
        projectCoreData.setUuid(entity.getProjectUuid());
        projectCoreData.setTitle(entity.getProjectTitle());
        projectCoreData.setDescription(entity.getProjectDescription());
        projectCoreData.setStartDate(localDateToDateType(entity.getStartDate().toLocalDate()));
        projectCoreData.setEndDate(localDateToDateType(entity.getEndDate().toLocalDate()));
        projectCoreData.setAdditionalFields(entity.getAdditionalFields());
        
        List<MilestoneAnalysis> milestoneList = entity.getMilestoneAnalyses().stream()
                .map(this::milestoneAnalysisEntityToDomain)
                .collect(Collectors.toList());
        
        return new ProjectAnalysis(projectCoreData, milestoneList);
    }

    public ProjectAnalysisEntity toEntity(ProjectAnalysis analysis) {
        if (analysis == null) {
            return null;
        }
        
        ProjectCoreData project = analysis.getProject();
        
        ProjectAnalysisEntity entity = new ProjectAnalysisEntity();
        entity.setProjectUuid(project.getUuid());
        entity.setProjectTitle(project.getTitle());
        entity.setProjectDescription(project.getDescription());
        
        // convert dateType dates into localDateTime
        entity.setStartDate(dateTypeToLocalDate(project.getStartDate()).atStartOfDay());
        entity.setEndDate(dateTypeToLocalDate(project.getEndDate()).atStartOfDay());

        // fixed here the error: additionalFields was not being initialized properly
        entity.setAdditionalFields(project.getAdditionalFields() != null ? 
            new java.util.HashMap<>(project.getAdditionalFields()) : new java.util.HashMap<>());
        
        List<MilestoneAnalysisEntity> milestoneEntities = analysis.getMilestoneList().stream()
                .map(this::milestoneAnalysisDomainToEntity)
                .collect(Collectors.toList());
        entity.setMilestoneAnalyses(milestoneEntities);
        
        return entity;
    }

    public List<ProjectAnalysis> toDomainList(List<ProjectAnalysisEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private MilestoneAnalysis milestoneAnalysisEntityToDomain(MilestoneAnalysisEntity entity) {
        List<TaskAnalysis> taskList = entity.getTaskAnalyses().stream()
                .map(this::taskAnalysisEntityToDomain)
                .collect(Collectors.toList());
        
        return new MilestoneAnalysis(
                entity.getMilestoneUuid(),
                entity.getMilestoneTitle(),
                localDateToDateType(entity.getStartDate()),
                localDateToDateType(entity.getEndDate()),
                entity.getInitialCompletion(),
                entity.getEndCompletion(),
                taskList
        );
    }

    private MilestoneAnalysisEntity milestoneAnalysisDomainToEntity(MilestoneAnalysis milestone) {
        List<TaskAnalysisEntity> taskEntities = milestone.getTaskList().stream()
                .map(this::taskAnalysisDomainToEntity)
                .collect(Collectors.toList());
        
        MilestoneAnalysisEntity entity = new MilestoneAnalysisEntity();
        entity.setMilestoneUuid(milestone.getMilestoneUuid());
        entity.setMilestoneTitle(milestone.getMilestoneTitle());
        entity.setStartDate(dateTypeToLocalDate(milestone.getStartDate()));
        entity.setEndDate(dateTypeToLocalDate(milestone.getEndDate()));
        entity.setInitialCompletion(milestone.getInitialCompletion());
        entity.setEndCompletion(milestone.getEndCompletion());
        entity.setTaskAnalyses(taskEntities);
        
        return entity;
    }

    private TaskAnalysis taskAnalysisEntityToDomain(TaskAnalysisEntity entity) {
        return new TaskAnalysis(
                entity.getTaskUuid(),
                entity.getTaskTitle(),
                entity.getInitialCompletion(),
                entity.getEndCompletion()
        );
    }

    private TaskAnalysisEntity taskAnalysisDomainToEntity(TaskAnalysis task) {
        TaskAnalysisEntity entity = new TaskAnalysisEntity();
        entity.setTaskUuid(task.getTaskUuid());
        entity.setTaskTitle(task.getTaskTitle());
        entity.setInitialCompletion(task.getInitialCompletion());
        entity.setEndCompletion(task.getEndCompletion());
        
        return entity;
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
