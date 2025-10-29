package com.projectManager.domain.milestone;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.projectManager.exception.InvalidArgumentException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MilestoneServiceImpl implements MilestoneService {
    private final MilestoneRepository milestoneRepository;

    @Override
    public List<Milestone> listMilestones() {
        log.debug("Fetching all milestones. Total count: {}", milestoneRepository.findAll().size());
        return milestoneRepository.findAll();
    }

    @Override
    public Milestone createMilestone(Milestone milestone) {
        log.debug("Creating new milestone: {}", milestone);
        validateMilestone(milestone);
        milestone.setUuid(UUID.randomUUID().toString());
        log.info("New milestone created with title: {}", milestone.getTitle());
        milestoneRepository.save(milestone);
        return milestone;
    }

    @Override
    public Milestone getMilestone(String uuid) {
        log.debug("Fetching milestone with ID: {}", uuid);
        Milestone milestone = milestoneRepository.findById(uuid).orElse(null);
        if (milestone == null) {
            log.warn("Milestone with ID: {} not found.", uuid);
            //throw exception
        }
        log.info("Milestone found: {}", milestone);
        return milestone;
    }

    @Override
    public Milestone updateMilestone(String uuid, Milestone milestone) {
        log.debug("Updating milestone with ID: {}", uuid);
        validateMilestone(milestone);
        milestone.setUuid(uuid);
        log.info("Milestone updated with title: {}", milestone.getTitle());
        milestoneRepository.update(milestone);
        return milestone;
    }

    @Override
    public void deleteMilestone(String uuid) {
        log.debug("Deleting milestone with ID: {}", uuid);
        Milestone milestone = milestoneRepository.findById(uuid).orElse(null);
        if (milestone == null) {
            log.warn("Milestone with ID: {} not found. Deletion aborted.", uuid);
            //throw exception
        }
        log.debug("Found milestone for deletion: {}", milestone);
        try {
            milestoneRepository.deleteById(uuid);
            log.info("Successfully deleted milestone with ID: {}", uuid);
        } catch (Exception e) {
            log.error("Error occurred while deleting milestone with ID: {}: {}", uuid, e.getMessage());
            //handle exception
        }
    }

    @Override
    public void validateMilestone(Milestone milestone) {
        if (milestone.getTitle() == null || milestone.getTitle().trim().isEmpty()) {
            log.warn("Attemp to create/update a milestone with an empty title.");
            throw new InvalidArgumentException("Milestone title is required and cannot be empty");
        }
        if (milestone.getDate() == null) {
            log.warn("Attemp to create/update a milestone with a null date.");
            throw new InvalidArgumentException("Milestone date is required");
        }
    }
}
