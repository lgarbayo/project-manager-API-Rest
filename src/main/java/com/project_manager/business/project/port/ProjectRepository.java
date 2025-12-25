package com.project_manager.business.project.port;

import com.project_manager.business.project.model.Milestone;
import com.project_manager.business.project.model.Project;
import com.project_manager.business.project.model.Task;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    // Project CRUD operations
    List<Project> findAll();
    Project save(Project project);
    Optional<Project> findById(String id);
    Project update(Project project);
    void deleteById(String id);

    // Task CRUD operations
    List<Task> findTasksByProjectUuid(String projectUuid);
    Task saveTask(Task task);
    Optional<Task> findTaskById(String taskId);
    Task updateTask(Task task);
    void deleteTaskById(String taskId);

    // Milestone CRUD operations
    List<Milestone> findMilestonesByProjectUuid(String projectUuid);
    Milestone saveMilestone(Milestone milestone);
    Optional<Milestone> findMilestoneById(String milestoneId);
    Milestone updateMilestone(Milestone milestone);
    void deleteMilestoneById(String milestoneId);
}
