package com.projectManager.domain.facade;

import com.projectManager.core.project.ProjectCoreData;
import com.projectManager.domain.analysis.ProjectAnalysis;
import com.projectManager.domain.project.Milestone;
import com.projectManager.domain.project.Project;
import com.projectManager.domain.project.Task;

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
}
