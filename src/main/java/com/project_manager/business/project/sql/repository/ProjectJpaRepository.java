package com.project_manager.business.project.sql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project_manager.business.project.sql.entity.ProjectEntity;

@Repository
public interface ProjectJpaRepository extends JpaRepository<ProjectEntity, String> {
}
