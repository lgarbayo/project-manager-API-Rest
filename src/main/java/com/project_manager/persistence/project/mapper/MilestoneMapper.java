package com.project_manager.persistence.project.mapper;

import com.project_manager.business.project.model.Milestone;
import com.project_manager.persistence.project.entity.MilestoneEntity;
import com.project_manager.shared.core.dateType.DateTypeMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = DateTypeMapper.class)
public interface MilestoneMapper {
    Milestone toDomain(MilestoneEntity entity);
    MilestoneEntity toEntity(Milestone milestone);
    List<Milestone> toDomainList(List<MilestoneEntity> entities);
    List<MilestoneEntity> toEntityList(List<Milestone> milestones);
}
