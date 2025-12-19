package com.project_manager.business.analysis.service;

import org.springframework.stereotype.Service;

import com.project_manager.business.analysis.model.TaskEstimation;

@Service
public interface TaskEstimationService {
    TaskEstimation estimateTask(String projectUuid, String taskUuid, String promptOverride);
}
