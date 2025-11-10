package com.project_manager.business.project.persistence.sql.repository;

import com.project_manager.business.project.persistence.sql.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectJpaRepository extends JpaRepository<ProjectEntity, String> {
}
