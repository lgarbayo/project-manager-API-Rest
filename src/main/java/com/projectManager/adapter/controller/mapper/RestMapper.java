package com.projectManager.adapter.controller.mapper;

import com.projectManager.adapter.controller.command.UpsertMilestoneCommand;
import com.projectManager.adapter.controller.command.UpsertProjectCommand;
import com.projectManager.adapter.controller.command.UpsertTaskCommand;
import com.projectManager.adapter.controller.response.*;
import com.projectManager.domain.analysis.MilestoneAnalysis;
import com.projectManager.domain.analysis.ProjectAnalysis;
import com.projectManager.domain.analysis.TaskAnalysis;
import com.projectManager.domain.milestone.Milestone;
import com.projectManager.domain.project.Project;
import com.projectManager.domain.task.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestMapper {
    
    // ===== PROJECT MAPPINGS =====
    
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
    
    // ===== MILESTONE MAPPINGS =====
    
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
    
    // ===== TASK MAPPINGS =====
    
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
        ProjectResponse projectResponse = toProjectResponse(analysis.getProject());
        
        List<MilestoneAnalysisResponse> milestoneResponses = analysis.getMilestoneList().stream()
            .map(this::toMilestoneAnalysisResponse)
            .collect(Collectors.toCollection(() -> new java.util.ArrayList<>()));
        
        return new ProjectAnalysisResponse(projectResponse, new java.util.ArrayList<>(milestoneResponses));
    }
    
    public MilestoneAnalysisResponse toMilestoneAnalysisResponse(MilestoneAnalysis analysis) {
        List<TaskAnalysisResponse> taskResponses = analysis.getTaskList().stream()
            .map(this::toTaskAnalysisResponse)
            .collect(Collectors.toCollection(() -> new java.util.ArrayList<>()));
        
        return new MilestoneAnalysisResponse(
            analysis.getMilestoneUuid(),
            analysis.getMilestoneTitle(),
            analysis.getStartDate(),
            analysis.getEndDate(),
            analysis.getInitialCompletion(),
            analysis.getEndCompletion(),
            new java.util.ArrayList<>(taskResponses)
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