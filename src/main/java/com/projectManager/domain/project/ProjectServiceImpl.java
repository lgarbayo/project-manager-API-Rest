package com.projectManager.domain.project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.projectManager.exception.InvalidArgumentException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{
    private final ProjectRepository projectRepository;

    @Override
    public List<Project> listProjects() {
        log.debug("Fetching all projects. Total count: {}", projectRepository.findAll().size());
        return projectRepository.findAll();
    }

    @Override
    public Project createProject(Project project) {
        log.debug("Creating new project: {}", project);
        validateProject(project);
        project.setUuid(UUID.randomUUID().toString());
        log.info("New project created with title: {}", project.getTitle());
        projectRepository.save(project);
        return project;
    }

    @Override
    public Project getProject(String uuid) {
        log.debug("Fetching project with ID: {}", uuid);
        Project project = projectRepository.findById(uuid).orElse(null);
        if (project == null) {
            log.warn("Project with ID: {} not found.", uuid);
            throw new InvalidArgumentException("Project not found");
        }
        log.info("Project found: {}", project);
        return project;
    }

    @Override
    public Project updateProject(String uuid, Project project) {
        log.debug("Updating project with ID: {}", uuid);
        validateProject(project);
        project.setUuid(uuid);
        log.info("Project updated with title: {}", project.getTitle());
        projectRepository.update(project);
        return project;
    }

    @Override
    public void deleteProject(String uuid) {
        log.debug("Attempting to delete project with ID: {}", uuid);
        Project project = projectRepository.findById(uuid).orElse(null);
        if (project == null) {
            log.warn("Project with ID: {} not found. Deletion aborted.", uuid);
            throw new InvalidArgumentException("Project not found");
        }
        log.debug("Found project for deletion: {}", project);
        try {
            projectRepository.deleteById(uuid);
            log.info("Successfully deleted project with ID: {}", uuid);
        } catch (Exception e) {
            log.error("Error occurred while deleting project with ID: {}: {}", uuid, e.getMessage());
            throw new InvalidArgumentException("Error occurred while deleting project");
        }
    }

    @Override
    public void validateProject(Project project) {
        if (project.getTitle() == null || project.getTitle().trim().isEmpty()) {
            log.warn("Attemp to create/update a project with an empty title.");
            throw new InvalidArgumentException("Project title is required and cannot be empty");
        }
        if (project.getStartDate() == null) {
            log.warn("Attemp to create/update a project with a null start date.");
            throw new InvalidArgumentException("Project start date is required");
        }
        if (project.getEndDate() == null) {
            log.warn("Attemp to create/update a project with a null end date.");
            throw new InvalidArgumentException("Project end date is required");
        }
    }
}