package com.projectManager.domain.project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    List<Project> findAll();
    void save(Project project);
    Optional<Project> findById(Long id);
    Project update(Project project);
    void deleteById(Long id);
}