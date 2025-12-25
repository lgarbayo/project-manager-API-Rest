package com.project_manager.persistence.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project_manager.persistence.analysis.entity.ProjectAnalysisEntity;

import java.util.List;

@Repository
// as i save analyses by a long id, i use long here instead of String uuid (for internal use)
public interface ProjectAnalysisJpaRepository extends JpaRepository<ProjectAnalysisEntity, Long> {
    List<ProjectAnalysisEntity> findAllByProjectUuid(String projectUuid);
    void deleteByProjectUuid(String projectUuid);
}
