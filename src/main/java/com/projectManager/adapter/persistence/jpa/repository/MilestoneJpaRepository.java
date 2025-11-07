package com.projectManager.adapter.persistence.jpa.repository;

import com.projectManager.adapter.persistence.entity.MilestoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MilestoneJpaRepository extends JpaRepository<MilestoneEntity, String> {
    List<MilestoneEntity> findByProjectUuid(String projectUuid);
}
