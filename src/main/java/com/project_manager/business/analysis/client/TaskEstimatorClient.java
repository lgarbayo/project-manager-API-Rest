package com.project_manager.business.analysis.client;

import com.project_manager.business.analysis.model.TaskEstimation;

public interface TaskEstimatorClient {
    TaskEstimation estimateTask(String projectUuid, String taskUuid, String prompt);
}
