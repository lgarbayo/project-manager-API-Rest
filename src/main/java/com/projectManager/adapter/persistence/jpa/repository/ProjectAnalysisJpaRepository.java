package com.projectManager.adapter.persistence.jpa.repository;

import com.projectManager.adapter.persistence.entity.ProjectAnalysisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// as i save analyses by a long id, i use long here instead of String uuid (for internal use)
public interface ProjectAnalysisJpaRepository extends JpaRepository<ProjectAnalysisEntity, Long> {
    List<ProjectAnalysisEntity> findAllByProjectUuid(String projectUuid);
    void deleteByProjectUuid(String projectUuid);
}
