package com.project_manager.business.analysis.service;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.project_manager.business.analysis.client.TaskDescriptionClient;
import com.project_manager.business.analysis.model.TaskDescriptionProposal;
import com.project_manager.business.facade.ProjectFacade;
import com.project_manager.business.project.model.Project;
import com.project_manager.business.project.model.Task;
import com.project_manager.shared.exception.ExternalServiceException;
import com.project_manager.shared.exception.InvalidArgumentException;
import com.project_manager.shared.exception.ManagerException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskDescriptionServiceImpl implements TaskDescriptionService {

    private final ObjectProvider<ProjectFacade> projectFacadeProvider;
    private final TaskDescriptionClient taskDescriptionClient;
    private final TaskPromptBuilder taskPromptBuilder;

    @Override
    public TaskDescriptionProposal describeTask(String projectUuid, String taskUuid, String promptOverride) {
        validate(projectUuid, taskUuid);

        ProjectFacade projectFacade = projectFacadeProvider.getObject();
        Project project = projectFacade.getProject(projectUuid);
        Task task = projectFacade.getTask(projectUuid, taskUuid);

        String prompt = taskPromptBuilder.buildDescriptionPrompt(project, task, promptOverride);
        TaskDescriptionProposal proposal = executeDescription(projectUuid, taskUuid, prompt);
        log.info("Received RAG description for project {} task {}", projectUuid, taskUuid);

        return proposal;
    }

    private TaskDescriptionProposal executeDescription(String projectUuid, String taskUuid, String prompt) {
        try {
            return taskDescriptionClient.describeTask(projectUuid, taskUuid, prompt);
        } catch (ManagerException ex) {
            log.error("External task description unavailable for project {} task {}: {}",
                    projectUuid, taskUuid, ex.getMessage());
            throw new ExternalServiceException(
                    "Task description service is temporarily unavailable. Please try again later.",
                    ex
            );
        }
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
