package com.projectManager.domain.project;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void createProject(Project project) {
        log.debug("Creating new project: {}", project);
        log.info("New project created with title: {}", project.getTitle());
        projectRepository.save(project);
    }

    @Override
    public Project getProject(Long uuid) {
        log.debug("Fetching project with ID: {}", uuid);
        Project project = projectRepository.findById(uuid).orElse(null);
        if (project == null) {
            log.warn("Project with ID: {} not found.", uuid);
            //throw exception
        }
        log.debug("Project found: {}", project);
        return project;
    }

    @Override
    public Project updateProject(Long uuid, Project project) {
        log.info("Updating project with ID: {}", uuid);
        return projectRepository.update(project);
    }

    @Override
    public void deleteProject(Long uuid) {
        log.info("Attempting to delete project with ID: {}", uuid);
        Project project = projectRepository.findById(uuid).orElse(null);
        if (project == null) {
            log.warn("Project with ID: {} not found. Deletion aborted.", uuid);
            //throw exception
        }
        log.debug("Found project for deletion: {}", project);
        try {
            projectRepository.deleteById(uuid);
            log.info("Successfully deleted project with ID: {}", uuid);
        } catch (Exception e) {
            log.error("Error occurred while deleting project with ID: {}: {}", uuid, e.getMessage());
            //handle exception
        }
        projectRepository.deleteById(uuid);
    }
}