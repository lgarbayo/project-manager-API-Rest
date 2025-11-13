package com.project_manager.persistence.project.mapper;

import com.project_manager.shared.core.dateType.DateType;
import com.project_manager.business.project.model.Milestone;
import com.project_manager.persistence.project.entity.MilestoneEntity;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MilestoneMapper {

    public Milestone toDomain(MilestoneEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Milestone milestone = new Milestone();
        milestone.setUuid(entity.getUuid());
        milestone.setProjectUuid(entity.getProjectUuid());
        milestone.setTitle(entity.getTitle());
        milestone.setDate(localDateToDateType(entity.getDate()));
        milestone.setDescription(entity.getDescription());
        
        return milestone;
    }

    public MilestoneEntity toEntity(Milestone milestone) {
        if (milestone == null) {
            return null;
        }
        
        MilestoneEntity entity = new MilestoneEntity();
        entity.setUuid(milestone.getUuid());
        entity.setProjectUuid(milestone.getProjectUuid());
        entity.setTitle(milestone.getTitle());
        entity.setDate(dateTypeToLocalDate(milestone.getDate()));
        entity.setDescription(milestone.getDescription());
        
        return entity;
    }

    public List<Milestone> toDomainList(List<MilestoneEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public List<MilestoneEntity> toEntityList(List<Milestone> milestones) {
        return milestones.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    private DateType localDateToDateType(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        
        DateType dateType = new DateType();
        dateType.setYear(localDate.getYear());
        dateType.setMonth(localDate.getMonthValue() - 1);
        
        int dayOfMonth = localDate.getDayOfMonth();
        int week = (dayOfMonth - 1) / 7;
        dateType.setWeek(Math.min(week, 3));
        
        return dateType;
    }

    private LocalDate dateTypeToLocalDate(DateType dateType) {
        if (dateType == null) {
            return null;
        }
        
        int month = dateType.getMonth() + 1;
        int dayOfMonth = (dateType.getWeek() * 7) + 1;
        
        int year = dateType.getYear();
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        int maxDayOfMonth = firstDayOfMonth.lengthOfMonth();
        dayOfMonth = Math.min(dayOfMonth, maxDayOfMonth);
        
        return LocalDate.of(year, month, dayOfMonth);
    }
}