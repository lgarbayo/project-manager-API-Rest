package com.project_manager.persistence.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project_manager.persistence.project.entity.TaskEntity;

import java.util.List;

@Repository
public interface TaskJpaRepository extends JpaRepository<TaskEntity, String> {
    List<TaskEntity> findByProjectUuid(String projectUuid);
}
