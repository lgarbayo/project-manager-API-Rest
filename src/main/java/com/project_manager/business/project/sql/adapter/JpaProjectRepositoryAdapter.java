package com.project_manager.business.project.sql.adapter;

import com.project_manager.business.project.model.Milestone;
import com.project_manager.business.project.model.Project;
import com.project_manager.business.project.model.Task;
import com.project_manager.business.project.port.ProjectRepository;
import com.project_manager.business.project.sql.entity.MilestoneEntity;
import com.project_manager.business.project.sql.entity.ProjectEntity;
import com.project_manager.business.project.sql.entity.TaskEntity;
import com.project_manager.business.project.sql.mapper.MilestoneMapper;
import com.project_manager.business.project.sql.mapper.ProjectMapper;
import com.project_manager.business.project.sql.mapper.TaskMapper;
import com.project_manager.business.project.sql.repository.MilestoneJpaRepository;
import com.project_manager.business.project.sql.repository.ProjectJpaRepository;
import com.project_manager.business.project.sql.repository.TaskJpaRepository;
import com.project_manager.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JpaProjectRepositoryAdapter implements ProjectRepository {

    private final ProjectJpaRepository projectJpaRepository;
    private final TaskJpaRepository taskJpaRepository;
    private final MilestoneJpaRepository milestoneJpaRepository;
    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper;
    private final MilestoneMapper milestoneMapper;

    @Override
    public List<Project> findAll() {
        log.debug("Finding all projects");
        List<Project> projects = projectMapper.toDomainList(projectJpaRepository.findAll());
        log.info("Found {} projects", projects.size());
        return projects;
    }

    @Override
    public Optional<Project> findById(String id) {
        if (id == null) {
            log.warn("Project ID is null");
            return Optional.empty();
        }
        log.debug("Finding project by id: {}", id);
        Optional<Project> project = projectJpaRepository.findById(id).map(projectMapper::toDomain);
        if (project.isPresent()) {
            log.debug("Project found: {}", project.get().getTitle());
        } else {
            log.warn("Project not found with id: {}", id);
        }
        return project;
    }

    @Override
    public Project save(Project project) {
        log.debug("Saving project: {} ({})", project.getTitle(), project.getUuid());
        ProjectEntity entity = projectMapper.toEntity(project);
        if (entity == null) {
            log.error("Failed to convert project to entity");
            throw new IllegalArgumentException("Milestone entity conversion failed");
        }
        ProjectEntity saved = projectJpaRepository.save(entity);
        Project savedProject = projectMapper.toDomain(saved);
        log.info("Project saved successfully: {}", savedProject.getTitle());
        return savedProject;
    }

    @Override
    public Project update(Project project) {
        log.debug("Updating project: {} ({})", project.getTitle(), project.getUuid());
        String projectUuid = project.getUuid();
        if (projectUuid == null) {
            log.error("Project UUID is null");
            throw new IllegalArgumentException("Project UUID cannot be null");
        }
        if (!projectJpaRepository.existsById(projectUuid)) {
            log.error("Attempted to update non-existent project: {}", projectUuid);
            throw new ResourceNotFoundException("Project not found with id: " + projectUuid);
        }
        log.info("Project updated successfully: {}", project.getTitle());
        return save(project);
    }

        @Override
    public void deleteById(String id) {
        log.debug("Deleting project with id: {}", id);
        if (id == null) {
            log.error("Project ID is null");
            throw new IllegalArgumentException("Project ID cannot be null");
        }
        if (projectJpaRepository.existsById(id)) {
            projectJpaRepository.deleteById(id);
            log.info("Project deleted successfully: {}", id);
        } else {
            log.warn("Attempted to delete non-existent project: {}", id);
        }
    }

    // Task methods
    @Override
    public List<Task> findTasksByProjectUuid(String projectUuid) {
        log.debug("Finding tasks for project: {}", projectUuid);
        List<Task> tasks = taskMapper.toDomainList(taskJpaRepository.findByProjectUuid(projectUuid));
        log.info("Found {} tasks for project: {}", tasks.size(), projectUuid);
        return tasks;
    }

    @Override
    public Task saveTask(Task task) {
        log.debug("Saving task: {} for project: {}", task.getTitle(), task.getProjectUuid());
        TaskEntity entity = taskMapper.toEntity(task);
        if (entity == null) {
            log.error("Failed to convert task to entity");
            throw new IllegalArgumentException("Task entity conversion failed");
        }
        TaskEntity saved = taskJpaRepository.save(entity);
        Task savedTask = taskMapper.toDomain(saved);
        log.info("Task saved successfully: {}", savedTask.getTitle());
        return savedTask;
    }

    @Override
    public Optional<Task> findTaskById(String taskId) {
        log.debug("Finding task by id: {}", taskId);
        if (taskId == null) {
            log.error("Task ID is null");
            throw new IllegalArgumentException("Task ID cannot be null");
        }
        Optional<Task> task = taskJpaRepository.findById(taskId).map(taskMapper::toDomain);
        if (task.isPresent()) {
            log.debug("Task found: {}", task.get().getTitle());
        } else {
            log.warn("Task not found with id: {}", taskId);
        }
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        log.debug("Updating task: {} ({})", task.getTitle(), task.getUuid());
        String taskUuid = task.getUuid();
        if (taskUuid == null) {
            log.error("Task UUID is null");
            throw new IllegalArgumentException("Task UUID cannot be null");
        }
        if (!taskJpaRepository.existsById(taskUuid)) {
            log.error("Attempted to update non-existent task: {}", taskUuid);
            throw new ResourceNotFoundException("Task not found with id: " + taskUuid);
        }
        log.info("Task updated successfully: {}", task.getTitle());
        return saveTask(task);
    }

    @Override
    public void deleteTaskById(String taskId) {
        log.debug("Deleting task with id: {}", taskId);
        if (taskId == null) {
            log.error("Task ID is null");
            throw new IllegalArgumentException("Task ID cannot be null");
        }
        if (taskJpaRepository.existsById(taskId)) {
            taskJpaRepository.deleteById(taskId);
            log.info("Task deleted successfully: {}", taskId);
        } else {
            log.error("Attempted to delete non-existent task: {}", taskId);
            throw new ResourceNotFoundException("Task not found with id: " + taskId);
        }
    }

    // Milestone methods
    @Override
    public List<Milestone> findMilestonesByProjectUuid(String projectUuid) {
        log.debug("Finding milestones for project: {}", projectUuid);
        List<Milestone> milestones = milestoneMapper.toDomainList(milestoneJpaRepository.findByProjectUuid(projectUuid));
        log.info("Found {} milestones for project: {}", milestones.size(), projectUuid);
        return milestones;
    }

    @Override
    public Milestone saveMilestone(Milestone milestone) {
        log.debug("Saving milestone: {} for project: {}", milestone.getTitle(), milestone.getProjectUuid());
        MilestoneEntity entity = milestoneMapper.toEntity(milestone);
        if (entity == null) {
            log.error("Failed to convert milestone to entity");
            throw new IllegalArgumentException("Milestone entity conversion failed");
        }
        MilestoneEntity saved = milestoneJpaRepository.save(entity);
        Milestone savedMilestone = milestoneMapper.toDomain(saved);
        log.info("Milestone saved successfully: {}", savedMilestone.getTitle());
        return savedMilestone;
    }

    @Override
    public Optional<Milestone> findMilestoneById(String milestoneId) {
        log.debug("Finding milestone by id: {}", milestoneId);
        if (milestoneId == null) {
            log.error("Milestone ID is null");
            throw new IllegalArgumentException("Milestone ID cannot be null");
        }
        Optional<Milestone> milestone = milestoneJpaRepository.findById(milestoneId).map(milestoneMapper::toDomain);
        if (milestone.isPresent()) {
            log.debug("Milestone found: {}", milestone.get().getTitle());
        } else {
            log.warn("Milestone not found with id: {}", milestoneId);
        }
        return milestone;
    }

    @Override
    public Milestone updateMilestone(Milestone milestone) {
        log.debug("Updating milestone: {} ({})", milestone.getTitle(), milestone.getUuid());
        String milestoneUuid = milestone.getUuid();
        if (milestoneUuid == null) {
            log.error("Milestone UUID is null");
            throw new IllegalArgumentException("Milestone UUID cannot be null");
        }
        if (!milestoneJpaRepository.existsById(milestoneUuid)) {
            log.error("Attempted to update non-existent milestone: {}", milestoneUuid);
            throw new ResourceNotFoundException("Milestone not found with id: " + milestoneUuid);
        }
        log.info("Milestone updated successfully: {}", milestone.getTitle());
        return saveMilestone(milestone);
    }

    @Override
    public void deleteMilestoneById(String milestoneId) {
        log.debug("Deleting milestone with id: {}", milestoneId);
        if (milestoneId == null) {
            log.error("Milestone ID is null");
            throw new IllegalArgumentException("Milestone ID cannot be null");
        }
        if (milestoneJpaRepository.existsById(milestoneId)) {
            milestoneJpaRepository.deleteById(milestoneId);
            log.info("Milestone deleted successfully: {}", milestoneId);
        } else {
            log.error("Attempted to delete non-existent milestone: {}", milestoneId);
            throw new ResourceNotFoundException("Milestone not found with id: " + milestoneId);
        }
    }
}
