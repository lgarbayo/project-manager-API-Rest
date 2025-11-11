package com.project_manager.rest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project_manager.rest.command.UpsertTaskCommand;
import com.project_manager.rest.mapper.RestMapper;
import com.project_manager.rest.response.TaskResponse;
import com.project_manager.business.facade.ProjectFacade;
import com.project_manager.business.project.model.Task;
import com.project_manager.shared.exception.InvalidArgumentException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/project/{projectUuid}/task")
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    private final ProjectFacade projectFacade;
    private final RestMapper restMapper;

    @GetMapping
    public List<TaskResponse> listTasks(@PathVariable String projectUuid) {
        log.debug("GET /project/{}/task - Retrieving tasks", projectUuid);
        List<Task> tasks = projectFacade.getTasksByProject(projectUuid);
        log.info("Retrieved {} tasks for project {}", tasks.size(), projectUuid);
        return restMapper.toTaskResponseList(tasks);
    }

    @PostMapping
    public TaskResponse createTask(
            @PathVariable String projectUuid,
            @RequestBody UpsertTaskCommand command
    ) {
        log.debug("POST /project/{}/task - Creating new task", projectUuid);
        
        if (command == null) {
            log.warn("Attempted to create task with null command for project {}", projectUuid);
            throw new InvalidArgumentException("Task data is required");
        }
        
        Task task = restMapper.toTask(command, projectUuid);
        Task createdTask = projectFacade.addTaskToProject(projectUuid, task);
        log.info("Task created successfully for project {}: {}", projectUuid, createdTask.getTitle());
        return restMapper.toTaskResponse(createdTask);
    }

    @GetMapping("/{taskUuid}")
    public TaskResponse getTask(
            @PathVariable String projectUuid,
            @PathVariable String taskUuid
    ) {
        log.debug("GET /project/{}/task/{} - Retrieving task", projectUuid, taskUuid);
        Task task = projectFacade.getTask(projectUuid, taskUuid);
        log.info("Task retrieved successfully for project {}: {}", projectUuid, task.getTitle());
        return restMapper.toTaskResponse(task);
    }

    @PutMapping("/{taskUuid}")
    public TaskResponse updateTask(
            @PathVariable String projectUuid,
            @PathVariable String taskUuid,
            @RequestBody UpsertTaskCommand command
    ) {
        log.debug("PUT /project/{}/task/{} - Updating task", projectUuid, taskUuid);
        
        if (command == null) {
            log.warn("Attempted to update task with null command for project {}", projectUuid);
            throw new InvalidArgumentException("Task data is required");
        }
        
        Task task = restMapper.toTask(command, projectUuid);
        Task updatedTask = projectFacade.updateTask(projectUuid, taskUuid, task);
        log.info("Task updated successfully for project {}: {}", projectUuid, updatedTask.getTitle());
        return restMapper.toTaskResponse(updatedTask);
    }

    @DeleteMapping("/{taskUuid}")
    public void deleteTask(
            @PathVariable String projectUuid,
            @PathVariable String taskUuid
    ) {
        log.debug("DELETE /project/{}/task/{} - Deleting task", projectUuid, taskUuid);
        projectFacade.deleteTask(projectUuid, taskUuid);
        log.info("Task deleted successfully for project {}: {}", projectUuid, taskUuid);
    }
}
