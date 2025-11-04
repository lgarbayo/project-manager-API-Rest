package com.projectManager.domain.project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.projectManager.exception.InvalidArgumentException;
import com.projectManager.exception.ResourceNotFoundException;
import com.projectManager.exception.ConflictException;
import com.projectManager.domain.milestone.MilestoneService;
import com.projectManager.domain.milestone.Milestone;
import com.projectManager.domain.task.TaskService;
import com.projectManager.domain.task.Task;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final MilestoneService milestoneService;
    private final TaskService taskService;

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
        
        // Verificar dependencias antes de eliminar
        checkProjectDependencies(uuid);
        
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
    
    private void checkProjectDependencies(String projectUuid) {
        // verify if there are associated milestones
        List<Milestone> projectMilestones = milestoneService.listMilestonesByProject(projectUuid);
        if (!projectMilestones.isEmpty()) {
            log.warn("Cannot delete project {} - has {} associated milestones", projectUuid, projectMilestones.size());
            throw new ConflictException("Cannot delete project with " + projectMilestones.size() + " associated milestones. Delete milestones first.");
        }

        // verify if there are associated tasks
        List<Task> projectTasks = taskService.listTasksByProject(projectUuid);
        if (!projectTasks.isEmpty()) {
            log.warn("Cannot delete project {} - has {} associated tasks", projectUuid, projectTasks.size());
            throw new ConflictException("Cannot delete project with " + projectTasks.size() + " associated tasks. Delete tasks first.");
        }
        
        log.debug("Project {} has no dependencies, safe to delete", projectUuid);
    }
}
