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
import com.projectManager.adapter.controller.mapper.RestMapper;
import com.projectManager.adapter.controller.response.ProjectResponse;
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
    private final RestMapper restMapper;
    
    @GetMapping
    public List<ProjectResponse> listProjects() {
        log.info("GET /project - Retrieving all projects");
        List<Project> projects = projectService.listProjects();
        log.debug("Projects retrieved: {}", projects.size());
        return restMapper.toProjectResponseList(projects);
    }

    @PostMapping
    public ProjectResponse create(@RequestBody UpsertProjectCommand command) {
        log.info("POST /project - Creating new project: {}", command != null ? command.getTitle() : "null");
        
        if (command == null) {
            log.warn("Attempted to create project with null command");
            throw new InvalidArgumentException("Project data is required");
        }
        
        Project project = restMapper.toProject(command);
        Project createdProject = projectService.createProject(project);
        log.debug("Project created: {}", createdProject.getTitle());
        return restMapper.toProjectResponse(createdProject);
    }

    @GetMapping("/{projectUuid}")
    public ProjectResponse getProject(@PathVariable String projectUuid) {
        log.info("GET /project/{projectUuid} - Retrieving project with UUID: {}", projectUuid);
        Project project = projectService.getProject(projectUuid);
        log.debug("Project retrieved: {}", project.getTitle());
        return restMapper.toProjectResponse(project);
    }

    @PutMapping("/{projectUuid}")
    public ProjectResponse updateProject(@PathVariable String projectUuid, @RequestBody UpsertProjectCommand command) {
        log.info("PUT /project/{projectUuid} - Updating project with UUID: {}", projectUuid);
        
        if (command == null) {
            log.warn("Attempted to update project {} with null command", projectUuid);
            throw new InvalidArgumentException("Project data is required");
        }
        
        Project project = restMapper.toProject(command);
        Project updatedProject = projectService.updateProject(projectUuid, project);
        log.debug("Project updated: {}", updatedProject.getTitle());
        return restMapper.toProjectResponse(updatedProject);
    }

    @DeleteMapping("/{projectUuid}")
    public void deleteProject(@PathVariable String projectUuid) {
        log.info("DELETE /project/{projectUuid} - Deleting project with UUID: {}", projectUuid);
        projectService.deleteProject(projectUuid);
        log.debug("Project deleted with UUID: {}", projectUuid);
    }
}
