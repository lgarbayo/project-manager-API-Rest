package com.project_manager.business.analysis.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.project_manager.business.analysis.client.TaskEstimatorClient;
import com.project_manager.business.analysis.model.TaskEstimation;
import com.project_manager.business.project.model.Project;
import com.project_manager.business.project.model.Task;
import com.project_manager.business.project.service.ProjectService;
import com.project_manager.shared.exception.InvalidArgumentException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskEstimationServiceImpl implements TaskEstimationService {

    private final ProjectService projectService;
    private final TaskEstimatorClient taskEstimatorClient;
    private final TaskPromptBuilder taskPromptBuilder;

    @Override
    public TaskEstimation estimateTask(String projectUuid, String taskUuid, String promptOverride) {
        validate(projectUuid, taskUuid);

        Project project = projectService.getProject(projectUuid);
        Task task = projectService.getTask(projectUuid, taskUuid);

        String prompt = taskPromptBuilder.buildPrompt(project, task, promptOverride);
        TaskEstimation estimation = taskEstimatorClient.estimateTask(projectUuid, taskUuid, prompt);
        log.info("Received RAG estimation for project {} task {} with hours {}", projectUuid, taskUuid,
                estimation != null ? estimation.getHours() : null);

        return estimation;
    }

    private void validate(String projectUuid, String taskUuid) {
        if (!StringUtils.hasText(projectUuid)) {
            throw new InvalidArgumentException("Project UUID is required");
        }
        if (!StringUtils.hasText(taskUuid)) {
            throw new InvalidArgumentException("Task UUID is required");
        }
    }
}
