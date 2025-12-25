package com.project_manager.business.analysis.client;

import com.project_manager.business.analysis.model.TaskDescriptionProposal;

public interface TaskDescriptionClient {
    TaskDescriptionProposal describeTask(String projectUuid, String taskUuid, String prompt);
}
