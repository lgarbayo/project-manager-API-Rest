package com.projectManager.adapter.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.projectManager.domain.project.ProjectService;
import com.projectManager.adapter.controller.dto.ProjectAnalysis;
import com.projectManager.domain.project.Project;

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
    public Project create(@RequestBody Project project) {
        log.info("POST /project - Creating new project: {}", project.getTitle());
        Project createdProject = projectService.createProject(project);
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
    public Project updateProject(@PathVariable String projectUuid, @RequestBody Project project) {
        log.info("PUT /project/{projectUuid} - Updating project with UUID: {}", projectUuid);
        Project updatedProject = projectService.updateProject(projectUuid, project);
        log.debug("Project updated: {}", updatedProject.getTitle());
        return updatedProject;
    }

    @DeleteMapping("/{projectUuid}")
    public void deleteProject(@PathVariable String projectUuid) {
        log.info("DELETE /project/{projectUuid} - Deleting project with UUID: {}", projectUuid);
        projectService.deleteProject(projectUuid);
        log.debug("Project deleted with UUID: {}", projectUuid);
    }

    /*
    @GetMapping("/{projectUuid}/analysis")
    public ProjectAnalysis analyze(@PathVariable String projectUuid){ 
        log.info("GET /project/{projectUuid}/analysis - Analyzing project with UUID: {}", projectUuid);
        // devolver project analysis
    }
    */
}