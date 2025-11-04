package com.projectManager.adapter.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectManager.adapter.controller.command.UpsertMilestoneCommand;
import com.projectManager.adapter.controller.mapper.RestMapper;
import com.projectManager.adapter.controller.response.MilestoneResponse;
import com.projectManager.domain.milestone.Milestone;
import com.projectManager.domain.milestone.MilestoneService;
import com.projectManager.exception.InvalidArgumentException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/project/{projectUuid}/milestone")
@RequiredArgsConstructor
@Slf4j
public class MilestoneController {
    private final MilestoneService milestoneService;
    private final RestMapper restMapper;

    @GetMapping
    public List<MilestoneResponse> listMilestones(@PathVariable String projectUuid) {
        log.info("GET /project/{}/milestone - Retrieving milestones", projectUuid);
        List<Milestone> milestones = milestoneService.listMilestonesByProject(projectUuid);
        log.debug("Milestones retrieved for project {}: {}", projectUuid, milestones.size());
        return restMapper.toMilestoneResponseList(milestones);
    }

    @PostMapping
    public MilestoneResponse createMilestone(
            @PathVariable String projectUuid,
            @RequestBody UpsertMilestoneCommand command
    ) {
        log.info("POST /project/{}/milestone - Creating new milestone", projectUuid);
        
        if (command == null) {
            log.warn("Attempted to create milestone with null command for project {}", projectUuid);
            throw new InvalidArgumentException("Milestone data is required");
        }
        
        Milestone milestone = restMapper.toMilestone(command, projectUuid);
        Milestone createdMilestone = milestoneService.createMilestone(milestone);
        log.debug("Milestone created for project {}: {}", projectUuid, createdMilestone.getTitle());
        return restMapper.toMilestoneResponse(createdMilestone);
    }

    @GetMapping("/{milestoneUuid}")
    public MilestoneResponse getMilestone(
            @PathVariable String projectUuid,
            @PathVariable String milestoneUuid
    ) {
        log.info("GET /project/{}/milestone/{} - Retrieving milestone", projectUuid, milestoneUuid);
        Milestone milestone = ensureMilestoneBelongsToProject(projectUuid, milestoneUuid);
        log.debug("Milestone retrieved for project {}: {}", projectUuid, milestone.getTitle());
        return restMapper.toMilestoneResponse(milestone);
    }

    @PutMapping("/{milestoneUuid}")
    public MilestoneResponse updateMilestone(
            @PathVariable String projectUuid,
            @PathVariable String milestoneUuid,
            @RequestBody UpsertMilestoneCommand command
    ) {
        log.info("PUT /project/{}/milestone/{} - Updating milestone", projectUuid, milestoneUuid);
        
        if (command == null) {
            log.warn("Attempted to update milestone with null command for project {}", projectUuid);
            throw new InvalidArgumentException("Milestone data is required");
        }
        
        ensureMilestoneBelongsToProject(projectUuid, milestoneUuid);
        Milestone milestone = restMapper.toMilestone(command, projectUuid);
        Milestone updatedMilestone = milestoneService.updateMilestone(milestoneUuid, milestone);
        log.debug("Milestone updated for project {}: {}", projectUuid, updatedMilestone.getTitle());
        return restMapper.toMilestoneResponse(updatedMilestone);
    }

    @DeleteMapping("/{milestoneUuid}")
    public void deleteMilestone(
            @PathVariable String projectUuid,
            @PathVariable String milestoneUuid
    ) {
        log.info("DELETE /project/{}/milestone/{} - Deleting milestone", projectUuid, milestoneUuid);
        ensureMilestoneBelongsToProject(projectUuid, milestoneUuid);
        milestoneService.deleteMilestone(milestoneUuid);
        log.debug("Milestone deleted for project {} with UUID {}", projectUuid, milestoneUuid);
    }

    private Milestone ensureMilestoneBelongsToProject(String projectUuid, String milestoneUuid) {
        Milestone milestone = milestoneService.getMilestone(milestoneUuid);
        if (milestone.getProjectUuid() == null || !projectUuid.equals(milestone.getProjectUuid())) {
            log.warn("Milestone {} does not belong to project {}", milestoneUuid, projectUuid);
            throw new InvalidArgumentException("Milestone does not belong to the specified project");
        }
        return milestone;
    }
}
