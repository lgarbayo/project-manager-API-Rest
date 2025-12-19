package com.project_manager.business.analysis.service;

import org.springframework.stereotype.Service;

import com.project_manager.business.analysis.model.TaskDescriptionProposal;

@Service
public interface TaskDescriptionService {
    TaskDescriptionProposal describeTask(String projectUuid, String taskUuid, String promptOverride);
}
