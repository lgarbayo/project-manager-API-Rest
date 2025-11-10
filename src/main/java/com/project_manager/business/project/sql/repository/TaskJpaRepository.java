package com.project_manager.business.project.persistence.sql.repository;

import com.project_manager.business.project.persistence.sql.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskJpaRepository extends JpaRepository<TaskEntity, String> {
    List<TaskEntity> findByProjectUuid(String projectUuid);
}
