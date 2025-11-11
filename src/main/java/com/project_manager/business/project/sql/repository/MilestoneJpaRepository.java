package com.project_manager.business.project.sql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project_manager.business.project.sql.entity.MilestoneEntity;

import java.util.List;

@Repository
public interface MilestoneJpaRepository extends JpaRepository<MilestoneEntity, String> {
    List<MilestoneEntity> findByProjectUuid(String projectUuid);
}
