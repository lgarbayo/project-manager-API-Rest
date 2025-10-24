package com.projectManager.domain.project;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {
    List<Project> listProjects();
    void createProject(Project project);
    Project getProject(Long uuid);
    Project updateProject(Long uuid, Project project);
    void deleteProject(Long uuid);
}