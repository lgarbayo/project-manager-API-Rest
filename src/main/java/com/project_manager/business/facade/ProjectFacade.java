package com.project_manager.business.facade;

import com.project_manager.shared.core.project.ProjectCoreData;
import com.project_manager.business.analysis.model.ProjectAnalysis;
import com.project_manager.business.analysis.model.TaskDescriptionProposal;
import com.project_manager.business.analysis.model.TaskEstimation;
import com.project_manager.business.project.model.Milestone;
import com.project_manager.business.project.model.Project;
import com.project_manager.business.project.model.Task;

import java.util.List;

public interface ProjectFacade {
    // Project operations
    List<Project> listProjects();
    Project createProject(Project project);
    Project getProject(String projectUuid);
    Project updateProject(String projectUuid, Project project);
    void deleteProject(String projectUuid);

    // Task operations
    List<Task> getTasksByProject(String projectUuid);
    Task addTaskToProject(String projectUuid, Task task);
    Task getTask(String projectUuid, String taskUuid);
    Task updateTask(String projectUuid, String taskUuid, Task task);
    void deleteTask(String projectUuid, String taskUuid);

    // Milestone operations
    List<Milestone> getMilestonesByProject(String projectUuid);
    Milestone addMilestoneToProject(String projectUuid, Milestone milestone);
    Milestone getMilestone(String projectUuid, String milestoneUuid);
    Milestone updateMilestone(String projectUuid, String milestoneUuid, Milestone milestone);
    void deleteMilestone(String projectUuid, String milestoneUuid);

    // Project core data & analysis
    ProjectCoreData getProjectCoreData(String projectUuid);
    ProjectAnalysis analyzeProject(String projectUuid);
    
    // Task estimation
    TaskEstimation estimateTask(String projectUuid, String taskUuid, String promptOverride);

    // Task description proposal
    TaskDescriptionProposal describeTask(String projectUuid, String taskUuid, String promptOverride);
}
