package com.projectManager.domain.project;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {
    List<Project> listProjects();
    Project createProject(Project project);
    Project getProject(String uuid);
    Project updateProject(String uuid, Project project);
    void deleteProject(String uuid);
}
