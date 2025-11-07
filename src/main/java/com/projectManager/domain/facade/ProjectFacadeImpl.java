package com.projectManager.domain.facade;

import org.springframework.stereotype.Component;

import com.projectManager.core.project.ProjectCoreData;
import com.projectManager.domain.project.milestone.Milestone;
import com.projectManager.domain.project.milestone.MilestoneService;
import com.projectManager.domain.project.Project;
import com.projectManager.domain.project.ProjectService;
import com.projectManager.domain.project.task.Task;
import com.projectManager.domain.project.task.TaskService;
import com.projectManager.exception.ConflictException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectFacadeImpl implements ProjectFacade {
    
    private final ProjectService projectService;
    private final MilestoneService milestoneService;
    private final TaskService taskService;
    
    @Override
    public void checkDependencies(String projectUuid) {
        if (projectUuid == null || projectUuid.trim().isEmpty()) {
            log.warn("Attempted to check dependencies with null or empty project UUID");
            throw new ConflictException("Project UUID cannot be null or empty");
        }
        
        log.debug("Checking dependencies for project: {}", projectUuid);
        
        List<Milestone> milestones = milestoneService.listMilestonesByProject(projectUuid);
        if (!milestones.isEmpty()) {
            log.warn("Cannot delete project {} - has {} associated milestones", projectUuid, milestones.size());
            throw new ConflictException("Cannot delete project: project has " + milestones.size() + " associated milestones");
        }

        List<Task> tasks = taskService.listTasksByProject(projectUuid);
        if (!tasks.isEmpty()) {
            log.warn("Cannot delete project {} - has {} associated tasks", projectUuid, tasks.size());
            throw new ConflictException("Cannot delete project: project has " + tasks.size() + " associated tasks");
        }
        
        log.debug("Project {} has no dependencies, safe to delete", projectUuid);
    }

    @Override
    public ProjectCoreData getProjectCoreData(String projectUuid) {
        log.debug("Getting project core data for: {}", projectUuid);
        Project project = projectService.getProject(projectUuid);
        return mapToProjectCoreData(project);
    }

    @Override
    public List<Milestone> getMilestonesByProject(String projectUuid) {
        log.debug("Getting milestones for project: {}", projectUuid);
        return milestoneService.listMilestonesByProject(projectUuid);
    }

    @Override
    public List<Task> getTasksByProject(String projectUuid) {
        log.debug("Getting tasks for project: {}", projectUuid);
        return taskService.listTasksByProject(projectUuid);
    }

    private ProjectCoreData mapToProjectCoreData(Project project) {
        return new ProjectCoreData(
            project.getUuid(),
            project.getTitle(),
            project.getDescription(),
            project.getStartDate(),
            project.getEndDate(),
            project.getAdditionalFields()
        );
    }
}
