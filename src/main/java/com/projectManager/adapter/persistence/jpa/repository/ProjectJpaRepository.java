package com.projectManager.adapter.persistence.jpa.repository;

import com.projectManager.adapter.persistence.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectJpaRepository extends JpaRepository<ProjectEntity, String> {
}
