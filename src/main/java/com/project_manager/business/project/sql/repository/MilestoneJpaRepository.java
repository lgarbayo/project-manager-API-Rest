package com.project_manager.business.project.persistence.sql.repository;

import com.project_manager.business.project.persistence.sql.entity.MilestoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MilestoneJpaRepository extends JpaRepository<MilestoneEntity, String> {
    List<MilestoneEntity> findByProjectUuid(String projectUuid);
}
