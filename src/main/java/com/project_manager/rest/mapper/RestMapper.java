package com.project_manager.rest.mapper;

import com.project_manager.rest.command.UpsertMilestoneCommand;
import com.project_manager.rest.command.UpsertProjectCommand;
import com.project_manager.rest.command.UpsertTaskCommand;
import com.project_manager.rest.response.*;
import com.project_manager.shared.core.project.ProjectCoreData;
import com.project_manager.business.analysis.model.MilestoneAnalysis;
import com.project_manager.business.analysis.model.ProjectAnalysis;
import com.project_manager.business.analysis.model.TaskAnalysis;
import com.project_manager.business.project.model.Milestone;
import com.project_manager.business.project.model.Project;
import com.project_manager.business.project.model.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestMapper {

    // project mappings

    public ProjectResponse toProjectResponse(Project project) {
        return new ProjectResponse(
            project.getUuid(),
            project.getTitle(),
            project.getDescription(),
            project.getStartDate(),
            project.getEndDate(),
            project.getAdditionalFields()
        );
    }
    
    public ProjectResponse toProjectResponseFromCoreData(ProjectCoreData projectCoreData) {
        return new ProjectResponse(
            projectCoreData.getUuid(),
            projectCoreData.getTitle(),
            projectCoreData.getDescription(),
            projectCoreData.getStartDate(),
            projectCoreData.getEndDate(),
            projectCoreData.getAdditionalFields()
        );
    }
    
    public List<ProjectResponse> toProjectResponseList(List<Project> projects) {
        return projects.stream()
            .map(this::toProjectResponse)
            .collect(Collectors.toList());
    }
    
    public Project toProject(UpsertProjectCommand command) {
        Project project = new Project();
        project.setTitle(command.getTitle());
        project.setDescription(command.getDescription());
        project.setStartDate(command.getStartDate());
        project.setEndDate(command.getEndDate());
        project.setAdditionalFields(command.getAdditionalFields());
        return project;
    }

    // milestone mappings

    public MilestoneResponse toMilestoneResponse(Milestone milestone) {
        return new MilestoneResponse(
            milestone.getUuid(),
            milestone.getProjectUuid(),
            milestone.getTitle(),
            milestone.getDescription(),
            milestone.getDate()
        );
    }
    
    public List<MilestoneResponse> toMilestoneResponseList(List<Milestone> milestones) {
        return milestones.stream()
            .map(this::toMilestoneResponse)
            .collect(Collectors.toList());
    }
    
    public Milestone toMilestone(UpsertMilestoneCommand command, String projectUuid) {
        Milestone milestone = new Milestone();
        milestone.setProjectUuid(projectUuid);
        milestone.setTitle(command.getTitle());
        milestone.setDescription(command.getDescription());
        milestone.setDate(command.getDate());
        return milestone;
    }

    // task mappings

    public TaskResponse toTaskResponse(Task task) {
        return new TaskResponse(
            task.getUuid(),
            task.getProjectUuid(),
            task.getTitle(),
            task.getDescription(),
            task.getDurationWeeks(),
            task.getStartDate()
        );
    }
    
    public List<TaskResponse> toTaskResponseList(List<Task> tasks) {
        return tasks.stream()
            .map(this::toTaskResponse)
            .collect(Collectors.toList());
    }
    
    public Task toTask(UpsertTaskCommand command, String projectUuid) {
        Task task = new Task();
        task.setProjectUuid(projectUuid);
        task.setTitle(command.getTitle());
        task.setDescription(command.getDescription());
        task.setDurationWeeks(command.getDurationWeeks());
        task.setStartDate(command.getStartDate());
        return task;
    }
    
    // ===== ANALYSIS MAPPINGS =====
    
    public ProjectAnalysisResponse toProjectAnalysisResponse(ProjectAnalysis analysis) {
        ProjectResponse projectResponse = toProjectResponseFromCoreData(analysis.getProject());
        
        List<MilestoneAnalysisResponse> milestoneResponses = analysis.getMilestoneList().stream()
            .map(this::toMilestoneAnalysisResponse)
            .collect(Collectors.toList());
        
        return new ProjectAnalysisResponse(projectResponse, milestoneResponses);
    }
    
    public MilestoneAnalysisResponse toMilestoneAnalysisResponse(MilestoneAnalysis analysis) {
        List<TaskAnalysisResponse> taskResponses = analysis.getTaskList().stream()
            .map(this::toTaskAnalysisResponse)
            .collect(Collectors.toList());
        
        return new MilestoneAnalysisResponse(
            analysis.getMilestoneUuid(),
            analysis.getMilestoneTitle(),
            analysis.getStartDate(),
            analysis.getEndDate(),
            analysis.getInitialCompletion(),
            analysis.getEndCompletion(),
            taskResponses
        );
    }
    
    public TaskAnalysisResponse toTaskAnalysisResponse(TaskAnalysis analysis) {
        return new TaskAnalysisResponse(
            analysis.getTaskUuid(),
            analysis.getTaskTitle(),
            analysis.getInitialCompletion(),
            analysis.getEndCompletion()
        );
    }
}