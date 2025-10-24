package com.projectManager.domain.milestone;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MilestoneService {
    List<Milestone> listMilestones();
    void createMilestone(Milestone milestone);
    Milestone getMilestone(Long id);
    void updateMilestone(Milestone milestone);
    void deleteMilestone(Long id);
}