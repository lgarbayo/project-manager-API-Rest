package com.project_manager.persistence.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project_manager.persistence.project.entity.ProjectEntity;

@Repository
public interface ProjectJpaRepository extends JpaRepository<ProjectEntity, String> {
}
