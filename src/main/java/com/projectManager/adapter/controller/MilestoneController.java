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
import com.projectManager.domain.facade.ProjectFacade;
import com.projectManager.domain.project.Milestone;
import com.projectManager.exception.InvalidArgumentException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/project/{projectUuid}/milestone")
@RequiredArgsConstructor
@Slf4j
public class MilestoneController {
    private final ProjectFacade projectFacade;
    private final RestMapper restMapper;

    @GetMapping
    public List<MilestoneResponse> listMilestones(@PathVariable String projectUuid) {
        log.debug("GET /project/{}/milestone - Retrieving milestones", projectUuid);
        List<Milestone> milestones = projectFacade.getMilestonesByProject(projectUuid);
        log.info("Retrieved {} milestones for project {}", milestones.size(), projectUuid);
        return restMapper.toMilestoneResponseList(milestones);
    }

    @PostMapping
    public MilestoneResponse createMilestone(
            @PathVariable String projectUuid,
            @RequestBody UpsertMilestoneCommand command
    ) {
        log.debug("POST /project/{}/milestone - Creating new milestone", projectUuid);
        
        if (command == null) {
            log.warn("Attempted to create milestone with null command for project {}", projectUuid);
            throw new InvalidArgumentException("Milestone data is required");
        }
        
        Milestone milestone = restMapper.toMilestone(command, projectUuid);
        Milestone createdMilestone = projectFacade.addMilestoneToProject(projectUuid, milestone);
        log.info("Milestone created successfully for project {}: {}", projectUuid, createdMilestone.getTitle());
        return restMapper.toMilestoneResponse(createdMilestone);
    }

    @GetMapping("/{milestoneUuid}")
    public MilestoneResponse getMilestone(
            @PathVariable String projectUuid,
            @PathVariable String milestoneUuid
    ) {
        log.debug("GET /project/{}/milestone/{} - Retrieving milestone", projectUuid, milestoneUuid);
        Milestone milestone = projectFacade.getMilestone(projectUuid, milestoneUuid);
        log.info("Milestone retrieved successfully for project {}: {}", projectUuid, milestone.getTitle());
        return restMapper.toMilestoneResponse(milestone);
    }

    @PutMapping("/{milestoneUuid}")
    public MilestoneResponse updateMilestone(
            @PathVariable String projectUuid,
            @PathVariable String milestoneUuid,
            @RequestBody UpsertMilestoneCommand command
    ) {
        log.debug("PUT /project/{}/milestone/{} - Updating milestone", projectUuid, milestoneUuid);
        
        if (command == null) {
            log.warn("Attempted to update milestone with null command for project {}", projectUuid);
            throw new InvalidArgumentException("Milestone data is required");
        }
        
        Milestone milestone = restMapper.toMilestone(command, projectUuid);
        Milestone updatedMilestone = projectFacade.updateMilestone(projectUuid, milestoneUuid, milestone);
        log.info("Milestone updated successfully for project {}: {}", projectUuid, updatedMilestone.getTitle());
        return restMapper.toMilestoneResponse(updatedMilestone);
    }

    @DeleteMapping("/{milestoneUuid}")
    public void deleteMilestone(
            @PathVariable String projectUuid,
            @PathVariable String milestoneUuid
    ) {
        log.debug("DELETE /project/{}/milestone/{} - Deleting milestone", projectUuid, milestoneUuid);
        projectFacade.deleteMilestone(projectUuid, milestoneUuid);
        log.info("Milestone deleted successfully for project {}: {}", projectUuid, milestoneUuid);
    }
}
