package com.projectManager.adapter.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectManager.adapter.controller.command.UpsertProjectCommand;
import com.projectManager.domain.project.ProjectService;
import com.projectManager.domain.project.Project;
import com.projectManager.exception.InvalidArgumentException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    private final ProjectService projectService;
    
    @GetMapping
    public List<Project> listProjects() {
        log.info("GET /project - Retrieving all projects");
        List<Project> projects = projectService.listProjects();
        log.debug("Projects retrieved: {}", projects.size());
        return projects;
    }

    @PostMapping
    public Project create(@RequestBody UpsertProjectCommand command) {
        log.info("POST /project - Creating new project: {}", command != null ? command.getTitle() : "null");
        Project createdProject = projectService.createProject(toProject(command));
        log.debug("Project created: {}", createdProject.getTitle());
        return createdProject;
    }

    @GetMapping("/{projectUuid}")
    public Project getProject(@PathVariable String projectUuid) {
        log.info("GET /project/{projectUuid} - Retrieving project with UUID: {}", projectUuid);
        Project project = projectService.getProject(projectUuid);
        log.debug("Project retrieved: {}", project.getTitle());
        return project;
    }

    @PutMapping("/{projectUuid}")
    public Project updateProject(@PathVariable String projectUuid, @RequestBody UpsertProjectCommand command) {
        log.info("PUT /project/{projectUuid} - Updating project with UUID: {}", projectUuid);
        Project updatedProject = projectService.updateProject(projectUuid, toProject(command));
        log.debug("Project updated: {}", updatedProject.getTitle());
        return updatedProject;
    }

    @DeleteMapping("/{projectUuid}")
    public void deleteProject(@PathVariable String projectUuid) {
        log.info("DELETE /project/{projectUuid} - Deleting project with UUID: {}", projectUuid);
        projectService.deleteProject(projectUuid);
        log.debug("Project deleted with UUID: {}", projectUuid);
    }

    private Project toProject(UpsertProjectCommand command) {
        if (command == null) {
            log.warn("Attempt to upsert a project with null command.");
            throw new InvalidArgumentException("Project data is required");
        }
        Project project = new Project();
        project.setTitle(command.getTitle());
        project.setDescription(command.getDescription());
        project.setStartDate(command.getStartDate());
        project.setEndDate(command.getEndDate());
        project.setAdditionalFields(command.getAdditionalFields());
        return project;
    }
}
