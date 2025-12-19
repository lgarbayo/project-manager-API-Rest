package com.project_manager.business.facade;

import com.project_manager.business.analysis.model.ProjectAnalysis;
import com.project_manager.business.analysis.model.TaskEstimation;
import com.project_manager.business.analysis.service.AnalysisService;
import com.project_manager.business.analysis.service.TaskEstimationService;
import com.project_manager.business.project.model.Milestone;
import com.project_manager.business.project.model.Project;
import com.project_manager.business.project.model.Task;
import com.project_manager.business.project.service.ProjectService;
import com.project_manager.shared.core.project.ProjectCoreData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectFacadeImpl implements ProjectFacade {
    
    private final ProjectService projectService;
    private final AnalysisService analysisService;
    private final TaskEstimationService taskEstimationService;
    
    @Override
    public List<Project> listProjects() {
        log.debug("Listing projects through facade");
        return projectService.listProjects();
    }

    @Override
    public Project createProject(Project project) {
        log.debug("Creating project through facade: {}", project != null ? project.getTitle() : "null");
        return projectService.createProject(project);
    }

    @Override
    public Project getProject(String projectUuid) {
        log.debug("Fetching project through facade: {}", projectUuid);
        return projectService.getProject(projectUuid);
    }

    @Override
    public Project updateProject(String projectUuid, Project project) {
        log.debug("Updating project through facade: {}", projectUuid);
        return projectService.updateProject(projectUuid, project);
    }

    @Override
    public void deleteProject(String projectUuid) {
        log.debug("Deleting project through facade: {}", projectUuid);
        projectService.deleteProject(projectUuid);
        log.info("Project {} deleted via facade", projectUuid);
    }

    @Override
    public List<Task> getTasksByProject(String projectUuid) {
        log.debug("Getting tasks for project: {}", projectUuid);
        return projectService.listTasks(projectUuid);
    }

    @Override
    public Task addTaskToProject(String projectUuid, Task task) {
        log.debug("Adding task to project {} through facade", projectUuid);
        return projectService.addTaskToProject(projectUuid, task);
    }

    @Override
    public Task getTask(String projectUuid, String taskUuid) {
        log.debug("Getting task {} for project {} through facade", taskUuid, projectUuid);
        return projectService.getTask(projectUuid, taskUuid);
    }

    @Override
    public Task updateTask(String projectUuid, String taskUuid, Task task) {
        log.debug("Updating task {} for project {} through facade", taskUuid, projectUuid);
        return projectService.updateTask(projectUuid, taskUuid, task);
    }

    @Override
    public void deleteTask(String projectUuid, String taskUuid) {
        log.debug("Deleting task {} for project {} through facade", taskUuid, projectUuid);
        projectService.deleteTask(projectUuid, taskUuid);
    }

    @Override
    public List<Milestone> getMilestonesByProject(String projectUuid) {
        log.debug("Getting milestones for project: {}", projectUuid);
        return projectService.listMilestones(projectUuid);
    }

    @Override
    public Milestone addMilestoneToProject(String projectUuid, Milestone milestone) {
        log.debug("Adding milestone to project {} through facade", projectUuid);
        return projectService.addMilestoneToProject(projectUuid, milestone);
    }

    @Override
    public Milestone getMilestone(String projectUuid, String milestoneUuid) {
        log.debug("Getting milestone {} for project {} through facade", milestoneUuid, projectUuid);
        return projectService.getMilestone(projectUuid, milestoneUuid);
    }

    @Override
    public Milestone updateMilestone(String projectUuid, String milestoneUuid, Milestone milestone) {
        log.debug("Updating milestone {} for project {} through facade", milestoneUuid, projectUuid);
        return projectService.updateMilestone(projectUuid, milestoneUuid, milestone);
    }

    @Override
    public void deleteMilestone(String projectUuid, String milestoneUuid) {
        log.debug("Deleting milestone {} for project {} through facade", milestoneUuid, projectUuid);
        projectService.deleteMilestone(projectUuid, milestoneUuid);
    }

    @Override
    public ProjectCoreData getProjectCoreData(String projectUuid) {
        log.debug("Getting project core data for: {}", projectUuid);
        Project project = projectService.getProject(projectUuid);
        return mapToProjectCoreData(project);
    }

    @Override
    public ProjectAnalysis analyzeProject(String projectUuid) {
        log.debug("Analyzing project through facade: {}", projectUuid);
        Project project = projectService.getProject(projectUuid);
        ProjectCoreData projectCoreData = mapToProjectCoreData(project);
        List<Milestone> milestones = projectService.listMilestones(projectUuid);
        List<Task> tasks = projectService.listTasks(projectUuid);
        return analysisService.analyzeProject(projectCoreData, milestones, tasks);
    }

    @Override
    public TaskEstimation estimateTask(String projectUuid, String taskUuid, String promptOverride) {
        log.debug("Requesting estimation for project {} task {}", projectUuid, taskUuid);
        return taskEstimationService.estimateTask(projectUuid, taskUuid, promptOverride);
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
