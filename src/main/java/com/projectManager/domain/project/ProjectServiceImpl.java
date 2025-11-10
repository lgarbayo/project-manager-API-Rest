package com.projectManager.domain.project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.projectManager.exception.InvalidArgumentException;
import com.projectManager.exception.ResourceNotFoundException;
import com.projectManager.exception.ConflictException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    @Override
    public List<Project> listProjects() {
        List<Project> projects = projectRepository.findAll();
        log.debug("Fetched {} projects.", projects.size());
        return projects;
    }

    @Override
    public Project createProject(Project project) {
        log.debug("Creating new project: {}", project);
        validateProject(project);
        project.setUuid(UUID.randomUUID().toString());
        Project savedProject = projectRepository.save(project);
        log.info("New project created with title: {}", savedProject.getTitle());
        return savedProject;
    }

    @Override
    public Project getProject(String uuid) {
        log.debug("Fetching project with ID: {}", uuid);
        validateUuid(uuid);
        Project project = projectRepository.findById(uuid).orElseThrow(() -> {
            log.warn("Project with ID: {} not found.", uuid);
            return new ResourceNotFoundException("Project not found with UUID: " + uuid);
        });
        log.info("Project found: {}", project);
        return project;
    }

    @Override
    public Project updateProject(String uuid, Project project) {
        log.debug("Updating project with ID: {}", uuid);
        validateUuid(uuid);
        validateProject(project);
        getProject(uuid);
        project.setUuid(uuid);
        Project updatedProject = projectRepository.update(project);
        log.info("Project updated with title: {}", updatedProject.getTitle());
        return updatedProject;
    }

    @Override
    public void deleteProject(String uuid) {
        log.debug("Attempting to delete project with ID: {}", uuid);
        validateUuid(uuid);
        Project project = getProject(uuid);
        log.debug("Found project for deletion: {}", project);
        try {
            projectRepository.deleteById(uuid);
            log.info("Successfully deleted project with ID: {}", uuid);
        } catch (Exception e) {
            log.error("Error occurred while deleting project with ID: {}", uuid, e);
            throw new ConflictException("Error occurred while deleting project: " + e.getMessage(), e);
        }
    }

    @Override
    public void validateProject(Project project) {
        if (project == null) {
            log.warn("Attempt to create or update a null project.");
            throw new InvalidArgumentException("Project is required");
        }
        if (project.getTitle() == null || project.getTitle().trim().isEmpty()) {
            log.warn("Attempt to create/update a project with an empty title.");
            throw new InvalidArgumentException("Project title is required and cannot be empty");
        }
        if (project.getStartDate() == null) {
            log.warn("Attempt to create/update a project with a null start date.");
            throw new InvalidArgumentException("Project start date is required");
        }
        if (project.getEndDate() == null) {
            log.warn("Attempt to create/update a project with a null end date.");
            throw new InvalidArgumentException("Project end date is required");
        }
    }

    private void validateUuid(String uuid) {
        if (uuid == null || uuid.trim().isEmpty()) {
            log.warn("Attempt to use an empty project UUID.");
            throw new InvalidArgumentException("Project UUID is required");
        }
    }

    // Task operations

    @Override
    public List<Task> listTasks(String projectUuid) {
        log.debug("Getting all tasks from project {}", projectUuid);
        validateUuid(projectUuid);
        getProject(projectUuid); // Verify project exists
        
        List<Task> tasks = projectRepository.findTasksByProjectUuid(projectUuid);
        log.debug("Found {} tasks in project {}", tasks.size(), projectUuid);
        return tasks;
    }

    @Override
    public Task addTaskToProject(String projectUuid, Task task) {
        log.debug("Adding task to project {}: {}", projectUuid, task);
        validateUuid(projectUuid);
        getProject(projectUuid); // Verify project exists
        validateTask(task);
        task.setProjectUuid(projectUuid);
        task.setUuid(UUID.randomUUID().toString());
        Task savedTask = projectRepository.saveTask(task);
        log.info("Task added to project {}: {}", projectUuid, savedTask.getTitle());
        return savedTask;
    }

    @Override
    public Task getTask(String projectUuid, String taskUuid) {
        log.debug("Getting task {} from project {}", taskUuid, projectUuid);
        validateUuid(projectUuid);
        validateTaskUuid(taskUuid);
        
        Task task = projectRepository.findTaskById(taskUuid).orElseThrow(() -> {
            log.warn("Task {} not found in project {}", taskUuid, projectUuid);
            return new ResourceNotFoundException("Task not found with UUID: " + taskUuid);
        });
        
        if (!projectUuid.equals(task.getProjectUuid())) {
            log.warn("Task {} does not belong to project {}", taskUuid, projectUuid);
            throw new InvalidArgumentException("Task does not belong to the specified project");
        }
        
        return task;
    }


    @Override
    public Task updateTask(String projectUuid, String taskUuid, Task task) {
        log.debug("Updating task {} in project {}", taskUuid, projectUuid);
        validateUuid(projectUuid);
        validateTaskUuid(taskUuid);
        getProject(projectUuid);
        validateTask(task);
        
        getTask(projectUuid, taskUuid);
        task.setUuid(taskUuid);
        task.setProjectUuid(projectUuid);
        Task updatedTask = projectRepository.updateTask(task);
        log.info("Task updated in project {}: {}", projectUuid, updatedTask.getTitle());
        return updatedTask;
    }

    @Override
    public void deleteTask(String projectUuid, String taskUuid) {
        log.debug("Deleting task {} from project {}", taskUuid, projectUuid);
        validateUuid(projectUuid);
        validateTaskUuid(taskUuid);
        
        Task task = getTask(projectUuid, taskUuid);
        try {
            projectRepository.deleteTaskById(taskUuid);
            log.info("Task deleted from project {}: {}", projectUuid, task.getTitle());
        } catch (Exception e) {
            log.error("Error deleting task {} from project {}", taskUuid, projectUuid, e);
            throw new ConflictException("Error occurred while deleting task: " + e.getMessage(), e);
        }
    }

    @Override
    public void validateTask(Task task) {
        if (task == null) {
            log.warn("Attempt to create or update a null task.");
            throw new InvalidArgumentException("Task is required");
        }
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            log.warn("Attempt to create/update a task with an empty title.");
            throw new InvalidArgumentException("Task title is required and cannot be empty");
        }
        if (task.getStartDate() == null) {
            log.warn("Attempt to create/update a task with a null start date.");
            throw new InvalidArgumentException("Task start date is required");
        }
        if (task.getDurationWeeks() <= 0) {
            log.warn("Attempt to create/update a task with non-positive duration.");
            throw new InvalidArgumentException("Task duration must be greater than 0");
        }
    }

    // Milestone operations

    @Override
    public List<Milestone> listMilestones(String projectUuid) {
        log.debug("Getting all milestones from project {}", projectUuid);
        validateUuid(projectUuid);
        getProject(projectUuid);
        
        List<Milestone> milestones = projectRepository.findMilestonesByProjectUuid(projectUuid);
        log.debug("Found {} milestones in project {}", milestones.size(), projectUuid);
        return milestones;
    }
    
    @Override
    public Milestone addMilestoneToProject(String projectUuid, Milestone milestone) {
        log.debug("Adding milestone to project {}: {}", projectUuid, milestone);
        validateUuid(projectUuid);
        getProject(projectUuid);
        validateMilestone(milestone);
        milestone.setProjectUuid(projectUuid);
        milestone.setUuid(UUID.randomUUID().toString());
        Milestone savedMilestone = projectRepository.saveMilestone(milestone);
        log.info("Milestone added to project {}: {}", projectUuid, savedMilestone.getTitle());
        return savedMilestone;
    }

    @Override
    public Milestone getMilestone(String projectUuid, String milestoneUuid) {
        log.debug("Getting milestone {} from project {}", milestoneUuid, projectUuid);
        validateUuid(projectUuid);
        validateMilestoneUuid(milestoneUuid);
        
        Milestone milestone = projectRepository.findMilestoneById(milestoneUuid).orElseThrow(() -> {
            log.warn("Milestone {} not found in project {}", milestoneUuid, projectUuid);
            return new ResourceNotFoundException("Milestone not found with UUID: " + milestoneUuid);
        });
        
        if (!projectUuid.equals(milestone.getProjectUuid())) {
            log.warn("Milestone {} does not belong to project {}", milestoneUuid, projectUuid);
            throw new InvalidArgumentException("Milestone does not belong to the specified project");
        }
        
        return milestone;
    }

    @Override
    public Milestone updateMilestone(String projectUuid, String milestoneUuid, Milestone milestone) {
        log.debug("Updating milestone {} in project {}", milestoneUuid, projectUuid);
        validateUuid(projectUuid);
        validateMilestoneUuid(milestoneUuid);
        getProject(projectUuid);
        validateMilestone(milestone);
        
        getMilestone(projectUuid, milestoneUuid);
        milestone.setUuid(milestoneUuid);
        milestone.setProjectUuid(projectUuid);
        Milestone updatedMilestone = projectRepository.updateMilestone(milestone);
        log.info("Milestone updated in project {}: {}", projectUuid, updatedMilestone.getTitle());
        return updatedMilestone;
    }

    @Override
    public void deleteMilestone(String projectUuid, String milestoneUuid) {
        log.debug("Deleting milestone {} from project {}", milestoneUuid, projectUuid);
        validateUuid(projectUuid);
        validateMilestoneUuid(milestoneUuid);
        
        Milestone milestone = getMilestone(projectUuid, milestoneUuid);
        try {
            projectRepository.deleteMilestoneById(milestoneUuid);
            log.info("Milestone deleted from project {}: {}", projectUuid, milestone.getTitle());
        } catch (Exception e) {
            log.error("Error deleting milestone {} from project {}", milestoneUuid, projectUuid, e);
            throw new ConflictException("Error occurred while deleting milestone: " + e.getMessage(), e);
        }
    }

    @Override
    public void validateMilestone(Milestone milestone) {
        if (milestone == null) {
            log.warn("Attempt to create or update a null milestone.");
            throw new InvalidArgumentException("Milestone is required");
        }
        if (milestone.getTitle() == null || milestone.getTitle().trim().isEmpty()) {
            log.warn("Attempt to create/update a milestone with an empty title.");
            throw new InvalidArgumentException("Milestone title is required and cannot be empty");
        }
        if (milestone.getDate() == null) {
            log.warn("Attempt to create/update a milestone with a null date.");
            throw new InvalidArgumentException("Milestone date is required");
        }
    }

    // aux methods
    
    private void validateTaskUuid(String taskUuid) {
        if (taskUuid == null || taskUuid.trim().isEmpty()) {
            log.warn("Attempt to use an empty task UUID.");
            throw new InvalidArgumentException("Task UUID is required");
        }
    }
    
    private void validateMilestoneUuid(String milestoneUuid) {
        if (milestoneUuid == null || milestoneUuid.trim().isEmpty()) {
            log.warn("Attempt to use an empty milestone UUID.");
            throw new InvalidArgumentException("Milestone UUID is required");
        }
    }
    
}
