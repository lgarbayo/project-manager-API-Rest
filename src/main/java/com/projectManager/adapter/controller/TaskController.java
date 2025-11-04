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

import com.projectManager.adapter.controller.command.UpsertTaskCommand;
import com.projectManager.domain.task.Task;
import com.projectManager.domain.task.TaskService;
import com.projectManager.exception.InvalidArgumentException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/project/{projectUuid}/task")
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public List<Task> listTasks(@PathVariable String projectUuid) {
        log.info("GET /project/{}/task - Retrieving tasks", projectUuid);
        List<Task> tasks = taskService.listTasksByProject(projectUuid);
        log.debug("Tasks retrieved for project {}: {}", projectUuid, tasks.size());
        return tasks;
    }

    @PostMapping
    public Task createTask(
            @PathVariable String projectUuid,
            @RequestBody UpsertTaskCommand command
    ) {
        log.info("POST /project/{}/task - Creating new task", projectUuid);
        Task createdTask = taskService.createTask(toTask(command, projectUuid));
        log.debug("Task created for project {}: {}", projectUuid, createdTask.getTitle());
        return createdTask;
    }

    @GetMapping("/{taskUuid}")
    public Task getTask(
            @PathVariable String projectUuid,
            @PathVariable String taskUuid
    ) {
        log.info("GET /project/{}/task/{} - Retrieving task", projectUuid, taskUuid);
        Task task = ensureTaskBelongsToProject(projectUuid, taskUuid);
        log.debug("Task retrieved for project {}: {}", projectUuid, task.getTitle());
        return task;
    }

    @PutMapping("/{taskUuid}")
    public Task updateTask(
            @PathVariable String projectUuid,
            @PathVariable String taskUuid,
            @RequestBody UpsertTaskCommand command
    ) {
        log.info("PUT /project/{}/task/{} - Updating task", projectUuid, taskUuid);
        ensureTaskBelongsToProject(projectUuid, taskUuid);
        Task updatedTask = taskService.updateTask(taskUuid, toTask(command, projectUuid));
        log.debug("Task updated for project {}: {}", projectUuid, updatedTask.getTitle());
        return updatedTask;
    }

    @DeleteMapping("/{taskUuid}")
    public void deleteTask(
            @PathVariable String projectUuid,
            @PathVariable String taskUuid
    ) {
        log.info("DELETE /project/{}/task/{} - Deleting task", projectUuid, taskUuid);
        ensureTaskBelongsToProject(projectUuid, taskUuid);
        taskService.deleteTask(taskUuid);
        log.debug("Task deleted for project {} with UUID {}", projectUuid, taskUuid);
    }

    private Task toTask(UpsertTaskCommand command, String projectUuid) {
        if (command == null) {
            log.warn("Attempt to upsert a task with null command for project {}", projectUuid);
            throw new InvalidArgumentException("Task data is required");
        }
        Task task = new Task();
        task.setProjectUuid(projectUuid);
        task.setTitle(command.getTitle());
        task.setDescription(command.getDescription());
        task.setDurationWeeks(command.getDurationWeeks());
        task.setStartDate(command.getStartDate());
        return task;
    }

    private Task ensureTaskBelongsToProject(String projectUuid, String taskUuid) {
        Task task = taskService.getTask(taskUuid);
        if (task.getProjectUuid() == null || !projectUuid.equals(task.getProjectUuid())) {
            log.warn("Task {} does not belong to project {}", taskUuid, projectUuid);
            throw new InvalidArgumentException("Task does not belong to the specified project");
        }
        return task;
    }
}
