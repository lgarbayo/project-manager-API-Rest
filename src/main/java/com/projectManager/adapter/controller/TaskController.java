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
import com.projectManager.adapter.controller.mapper.RestMapper;
import com.projectManager.adapter.controller.response.TaskResponse;
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
    private final RestMapper restMapper;

    @GetMapping
    public List<TaskResponse> listTasks(@PathVariable String projectUuid) {
        log.info("GET /project/{}/task - Retrieving tasks", projectUuid);
        List<Task> tasks = taskService.listTasksByProject(projectUuid);
        log.debug("Tasks retrieved for project {}: {}", projectUuid, tasks.size());
        return restMapper.toTaskResponseList(tasks);
    }

    @PostMapping
    public TaskResponse createTask(
            @PathVariable String projectUuid,
            @RequestBody UpsertTaskCommand command
    ) {
        log.info("POST /project/{}/task - Creating new task", projectUuid);
        
        if (command == null) {
            log.warn("Attempted to create task with null command for project {}", projectUuid);
            throw new InvalidArgumentException("Task data is required");
        }
        
        Task task = restMapper.toTask(command, projectUuid);
        Task createdTask = taskService.createTask(task);
        log.debug("Task created for project {}: {}", projectUuid, createdTask.getTitle());
        return restMapper.toTaskResponse(createdTask);
    }

    @GetMapping("/{taskUuid}")
    public TaskResponse getTask(
            @PathVariable String projectUuid,
            @PathVariable String taskUuid
    ) {
        log.info("GET /project/{}/task/{} - Retrieving task", projectUuid, taskUuid);
        Task task = ensureTaskBelongsToProject(projectUuid, taskUuid);
        log.debug("Task retrieved for project {}: {}", projectUuid, task.getTitle());
        return restMapper.toTaskResponse(task);
    }

    @PutMapping("/{taskUuid}")
    public TaskResponse updateTask(
            @PathVariable String projectUuid,
            @PathVariable String taskUuid,
            @RequestBody UpsertTaskCommand command
    ) {
        log.info("PUT /project/{}/task/{} - Updating task", projectUuid, taskUuid);
        
        if (command == null) {
            log.warn("Attempted to update task with null command for project {}", projectUuid);
            throw new InvalidArgumentException("Task data is required");
        }
        
        ensureTaskBelongsToProject(projectUuid, taskUuid);
        Task task = restMapper.toTask(command, projectUuid);
        Task updatedTask = taskService.updateTask(taskUuid, task);
        log.debug("Task updated for project {}: {}", projectUuid, updatedTask.getTitle());
        return restMapper.toTaskResponse(updatedTask);
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

    private Task ensureTaskBelongsToProject(String projectUuid, String taskUuid) {
        Task task = taskService.getTask(taskUuid);
        if (task.getProjectUuid() == null || !projectUuid.equals(task.getProjectUuid())) {
            log.warn("Task {} does not belong to project {}", taskUuid, projectUuid);
            throw new InvalidArgumentException("Task does not belong to the specified project");
        }
        return task;
    }
}
