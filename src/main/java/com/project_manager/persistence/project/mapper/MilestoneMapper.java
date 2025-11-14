package com.project_manager.persistence.project.mapper;

import com.project_manager.business.project.model.Milestone;
<<<<<<< HEAD:src/main/java/com/project_manager/persistence/project/mapper/MilestoneMapper.java
import com.project_manager.persistence.project.entity.MilestoneEntity;
=======
import com.project_manager.business.project.sql.entity.MilestoneEntity;
import com.project_manager.shared.core.dateType.DateTypeMapper;
import org.mapstruct.Mapper;
>>>>>>> 83c27c6 (feat: included mapstructs):src/main/java/com/project_manager/business/project/sql/mapper/MilestoneMapper.java

import java.util.List;

@Mapper(componentModel = "spring", uses = DateTypeMapper.class)
public interface MilestoneMapper {
    Milestone toDomain(MilestoneEntity entity);
    MilestoneEntity toEntity(Milestone milestone);
    List<Milestone> toDomainList(List<MilestoneEntity> entities);
    List<MilestoneEntity> toEntityList(List<Milestone> milestones);
}
