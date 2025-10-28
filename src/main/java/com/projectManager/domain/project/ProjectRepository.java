package com.projectManager.domain.project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    List<Project> findAll();
    Project save(Project project);
    Optional<Project> findById(String id);
    Project update(Project project);
    void deleteById(String id);
}
