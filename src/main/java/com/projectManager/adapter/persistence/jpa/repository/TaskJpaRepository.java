package com.projectManager.adapter.persistence.jpa.repository;

import com.projectManager.adapter.persistence.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskJpaRepository extends JpaRepository<TaskEntity, String> {
    List<TaskEntity> findByProjectUuid(String projectUuid);
}
