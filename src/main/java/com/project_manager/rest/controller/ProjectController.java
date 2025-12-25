package com.project_manager.rest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project_manager.rest.command.UpsertProjectCommand;
import com.project_manager.rest.mapper.RestMapper;
import com.project_manager.rest.response.ProjectResponse;
import com.project_manager.business.facade.ProjectFacade;
import com.project_manager.business.project.model.Project;
import com.project_manager.shared.exception.InvalidArgumentException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    private final ProjectFacade projectFacade;
    private final RestMapper restMapper;
    
    @GetMapping
    public List<ProjectResponse> listProjects() {
        log.debug("GET /project - Retrieving all projects");
        List<Project> projects = projectFacade.listProjects();
        log.info("Retrieved {} projects successfully", projects.size());
        return restMapper.toProjectResponseList(projects);
    }

    @PostMapping
    public ProjectResponse create(@RequestBody UpsertProjectCommand command) {
        log.debug("POST /project - Creating new project: {}", command != null ? command.getTitle() : "null");
        
        if (command == null) {
            log.warn("Attempted to create project with null command");
            throw new InvalidArgumentException("Project data is required");
        }
        
        Project project = restMapper.toProject(command);
        Project createdProject = projectFacade.createProject(project);
        log.info("Project created successfully: {}", createdProject.getTitle());
        return restMapper.toProjectResponse(createdProject);
    }

    @GetMapping("/{projectUuid}")
    public ProjectResponse getProject(@PathVariable String projectUuid) {
        log.debug("GET /project/{} - Retrieving project", projectUuid);
        Project project = projectFacade.getProject(projectUuid);
        log.info("Project retrieved successfully: {}", project.getTitle());
        return restMapper.toProjectResponse(project);
    }

    @PutMapping("/{projectUuid}")
    public ProjectResponse updateProject(@PathVariable String projectUuid, @RequestBody UpsertProjectCommand command) {
        log.debug("PUT /project/{} - Updating project", projectUuid);
        
        if (command == null) {
            log.warn("Attempted to update project {} with null command", projectUuid);
            throw new InvalidArgumentException("Project data is required");
        }
        
        Project project = restMapper.toProject(command);
        Project updatedProject = projectFacade.updateProject(projectUuid, project);
        log.info("Project updated successfully: {}", updatedProject.getTitle());
        return restMapper.toProjectResponse(updatedProject);
    }

    @DeleteMapping("/{projectUuid}")
    public void deleteProject(@PathVariable String projectUuid) {
        log.debug("DELETE /project/{} - Deleting project", projectUuid);
        projectFacade.deleteProject(projectUuid);
        log.info("Project deleted successfully: {}", projectUuid);
    }
}
