package com.project_manager.business.project.sql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project_manager.business.project.sql.entity.TaskEntity;

import java.util.List;

@Repository
public interface TaskJpaRepository extends JpaRepository<TaskEntity, String> {
    List<TaskEntity> findByProjectUuid(String projectUuid);
}
