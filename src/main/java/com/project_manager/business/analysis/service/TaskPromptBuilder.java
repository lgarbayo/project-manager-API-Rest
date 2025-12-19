package com.project_manager.business.analysis.service;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.project_manager.business.project.model.Project;
import com.project_manager.business.project.model.Task;
import com.project_manager.shared.core.dateType.DateType;

@Component
public class TaskPromptBuilder {

    public String buildEstimationPrompt(Project project, Task task, String promptOverride) {
        if (StringUtils.hasText(promptOverride)) {
            return promptOverride.trim();
        }

        String context = baseContext(project, task);
        return ("""
                You are an assistant that estimates engineering tasks.
                %s

                Provide an estimated effort for the task in hours along with a short explanation of the reasoning.
                """.formatted(context)).trim();
    }

    public String buildDescriptionPrompt(Project project, Task task, String promptOverride) {
        if (StringUtils.hasText(promptOverride)) {
            return promptOverride.trim();
        }

        String context = baseContext(project, task);
        return ("""
                You are an assistant that improves engineering task descriptions.
                %s

                Propose a concise title and a detailed description that developers can copy into the task, written in the same language as the input.
                """.formatted(context)).trim();
    }

    private String baseContext(Project project, Task task) {
        String projectDescription = defaultIfBlank(project.getDescription(), "No project description provided.");
        String taskDescription = defaultIfBlank(task.getDescription(), "No task description provided.");
        String formattedStartDate = formatDate(task.getStartDate());

        return ("""
                Project title: %s
                Project description: %s
                Task title: %s
                Task description: %s
                Task duration (weeks): %d
                Task start date: %s
                """.formatted(
                project.getTitle(),
                projectDescription,
                task.getTitle(),
                taskDescription,
                task.getDurationWeeks(),
                formattedStartDate
        )).trim();
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
