package com.projectManager.domain.milestone;

import java.util.List;
import java.util.Optional;

public interface MilestoneRepository {
    List<Milestone> findAll();
    List<Milestone> findByProjectUuid(String projectUuid);
    Milestone save(Milestone milestone);
    Optional<Milestone> findById(String id);
    Milestone update(Milestone milestone);
    void deleteById(String id);
}
