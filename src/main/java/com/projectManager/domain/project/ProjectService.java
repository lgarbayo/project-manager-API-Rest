package com.projectManager.domain.project;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {
    // Project CRUD operations
    List<Project> listProjects();
    Project createProject(Project project);
    Project getProject(String uuid);
    Project updateProject(String uuid, Project project);
    void deleteProject(String uuid);
    void validateProject(Project project);
    
    // CRUD operations for nested Task elements
    List<Task> listTasks(String projectUuid);
    Task addTaskToProject(String projectUuid, Task task);
    Task getTask(String projectUuid, String taskUuid);
    Task updateTask(String projectUuid, String taskUuid, Task task);
    void deleteTask(String projectUuid, String taskUuid);
    void validateTask(Task task);
    
    // CRUD operations for nested Milestone elements
    List<Milestone> listMilestones(String projectUuid);
    Milestone addMilestoneToProject(String projectUuid, Milestone milestone);
    Milestone getMilestone(String projectUuid, String milestoneUuid);
    Milestone updateMilestone(String projectUuid, String milestoneUuid, Milestone milestone);
    void deleteMilestone(String projectUuid, String milestoneUuid);
    void validateMilestone(Milestone milestone);
}
