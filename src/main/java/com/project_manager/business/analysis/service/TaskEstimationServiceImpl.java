package com.project_manager.business.analysis.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.project_manager.business.analysis.client.OpenApiRagClient;
import com.project_manager.business.analysis.client.dto.RagEstimateRequest;
import com.project_manager.business.analysis.client.dto.RagEstimateResponse;
import com.project_manager.business.analysis.model.TaskEstimation;
import com.project_manager.business.project.model.Project;
import com.project_manager.business.project.model.Task;
import com.project_manager.business.project.service.ProjectService;
import com.project_manager.shared.core.dateType.DateType;
import com.project_manager.shared.exception.InvalidArgumentException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskEstimationServiceImpl implements TaskEstimationService {

    private final ProjectService projectService;
    private final OpenApiRagClient ragClient;

    @Override
    public TaskEstimation estimateTask(String projectUuid, String taskUuid, String promptOverride) {
        validate(projectUuid, taskUuid);

        Project project = projectService.getProject(projectUuid);
        Task task = projectService.getTask(projectUuid, taskUuid);

        String prompt = buildPrompt(project, task, promptOverride);
        RagEstimateRequest request = RagEstimateRequest.builder()
                .projectUuid(projectUuid)
                .taskUuid(taskUuid)
                .prompt(prompt)
                .build();

        RagEstimateResponse response = ragClient.estimateTask(request);
        log.info("Received RAG estimation for project {} task {} with minutes {}", projectUuid, taskUuid,
                response != null ? response.getMinutes() : null);

        return TaskEstimation.builder()
                .projectUuid(projectUuid)
                .taskUuid(taskUuid)
                .prompt(prompt)
                .minutes(response != null ? response.getMinutes() : null)
                .explanation(response != null ? response.getExplanation() : null)
                .rawAnswer(response != null ? response.getRawAnswer() : null)
                .build();
    }

    private void validate(String projectUuid, String taskUuid) {
        if (!StringUtils.hasText(projectUuid)) {
            throw new InvalidArgumentException("Project UUID is required");
        }
        if (!StringUtils.hasText(taskUuid)) {
            throw new InvalidArgumentException("Task UUID is required");
        }
    }

    private String buildPrompt(Project project, Task task, String promptOverride) {
        if (StringUtils.hasText(promptOverride)) {
            return promptOverride.trim();
        }

        String projectDescription = defaultIfBlank(project.getDescription(), "No project description provided.");
        String taskDescription = defaultIfBlank(task.getDescription(), "No task description provided.");
        String formattedStartDate = formatDate(task.getStartDate());

        return """
                You are an assistant that estimates engineering tasks.
                Project title: %s
                Project description: %s
                Task title: %s
                Task description: %s
                Task duration (weeks): %d
                Task start date: %s

                Provide an estimated effort for the task in minutes along with a short explanation of the reasoning.
                """.formatted(
                project.getTitle(),
                projectDescription,
                task.getTitle(),
                taskDescription,
                task.getDurationWeeks(),
                formattedStartDate
        ).trim();
    }

    private String formatDate(DateType date) {
        if (date == null) {
            return "Unknown start date";
        }
        int month = date.getMonth() + 1;
        int week = date.getWeek() + 1;
        return "%04d-%02d (week %d)".formatted(date.getYear(), month, week);
    }

    private String defaultIfBlank(String value, String fallback) {
        return StringUtils.hasText(value) ? value.trim() : fallback;
    }
}
