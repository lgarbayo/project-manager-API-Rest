package com.projectManager.domain.milestone;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MilestoneService {
    List<Milestone> listMilestones();
    List<Milestone> listMilestonesByProject(String projectUuid);
    Milestone createMilestone(Milestone milestone);
    Milestone getMilestone(String uuid);
    Milestone updateMilestone(String uuid, Milestone milestone);
    void deleteMilestone(String uuid);
    void validateMilestone(Milestone milestone);
}
