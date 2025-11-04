package com.projectManager.domain.milestone;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.projectManager.exception.InvalidArgumentException;
import com.projectManager.exception.ResourceNotFoundException;
import com.projectManager.exception.ConflictException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MilestoneServiceImpl implements MilestoneService {
    private final MilestoneRepository milestoneRepository;

    @Override
    public List<Milestone> listMilestones() {
        List<Milestone> milestones = milestoneRepository.findAll();
        log.debug("Fetched {} milestones.", milestones.size());
        return milestones;
    }

    @Override
    public List<Milestone> listMilestonesByProject(String projectUuid) {
        log.debug("Fetching milestones for project ID: {}", projectUuid);
        validateProjectUuid(projectUuid);
        List<Milestone> milestones = milestoneRepository.findByProjectUuid(projectUuid);
        log.debug("Fetched {} milestones for project {}.", milestones.size(), projectUuid);
        return milestones;
    }

    @Override
    public Milestone createMilestone(Milestone milestone) {
        log.debug("Creating new milestone: {}", milestone);
        validateMilestone(milestone);
        milestone.setUuid(UUID.randomUUID().toString());
        Milestone savedMilestone = milestoneRepository.save(milestone);
        log.info("New milestone created with title: {}", savedMilestone.getTitle());
        return savedMilestone;
    }

    @Override
    public Milestone getMilestone(String uuid) {
        log.debug("Fetching milestone with ID: {}", uuid);
        validateUuid(uuid);
        Milestone milestone = milestoneRepository.findById(uuid).orElseThrow(() -> {
            log.warn("Milestone with ID: {} not found.", uuid);
            return new ResourceNotFoundException("Milestone not found with UUID: " + uuid);
        });
        log.info("Milestone found: {}", milestone);
        return milestone;
    }

    @Override
    public Milestone updateMilestone(String uuid, Milestone milestone) {
        log.debug("Updating milestone with ID: {}", uuid);
        validateUuid(uuid);
        validateMilestone(milestone);
        getMilestone(uuid);
        milestone.setUuid(uuid);
        Milestone updatedMilestone = milestoneRepository.update(milestone);
        log.info("Milestone updated with title: {}", updatedMilestone.getTitle());
        return updatedMilestone;
    }

    @Override
    public void deleteMilestone(String uuid) {
        log.debug("Deleting milestone with ID: {}", uuid);
        validateUuid(uuid);
        Milestone milestone = getMilestone(uuid);
        log.debug("Found milestone for deletion: {}", milestone);
        try {
            milestoneRepository.deleteById(uuid);
            log.info("Successfully deleted milestone with ID: {}", uuid);
        } catch (Exception e) {
            log.error("Error occurred while deleting milestone with ID: {}", uuid, e);
            throw new ConflictException("Error occurred while deleting milestone: " + e.getMessage(), e);
        }
    }

    @Override
    public void validateMilestone(Milestone milestone) {
        if (milestone == null) {
            log.warn("Attempt to create or update a null milestone.");
            throw new InvalidArgumentException("Milestone is required");
        }
        if (milestone.getProjectUuid() == null || milestone.getProjectUuid().trim().isEmpty()) {
            log.warn("Attempt to create/update a milestone without a project UUID.");
            throw new InvalidArgumentException("Milestone project UUID is required");
        }
        if (milestone.getTitle() == null || milestone.getTitle().trim().isEmpty()) {
            log.warn("Attempt to create/update a milestone with an empty title.");
            throw new InvalidArgumentException("Milestone title is required and cannot be empty");
        }
        if (milestone.getDate() == null) {
            log.warn("Attempt to create/update a milestone with a null date.");
            throw new InvalidArgumentException("Milestone date is required");
        }
    }

    private void validateUuid(String uuid) {
        if (uuid == null || uuid.trim().isEmpty()) {
            log.warn("Attempt to use an empty milestone UUID.");
            throw new InvalidArgumentException("Milestone UUID is required");
        }
    }

    private void validateProjectUuid(String projectUuid) {
        if (projectUuid == null || projectUuid.trim().isEmpty()) {
            log.warn("Attempt to use an empty project UUID for milestone operations.");
            throw new InvalidArgumentException("Project UUID is required");
        }
    }
}
