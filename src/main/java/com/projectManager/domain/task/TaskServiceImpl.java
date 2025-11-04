package com.projectManager.domain.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.projectManager.exception.InvalidArgumentException;
import com.projectManager.exception.ResourceNotFoundException;
import com.projectManager.exception.ConflictException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    
    @Override
    public List<Task> listTasks() {
        List<Task> tasks = taskRepository.findAll();
        log.debug("Fetched {} tasks.", tasks.size());
        return tasks;
    }

    @Override
    public List<Task> listTasksByProject(String projectUuid) {
        log.debug("Fetching tasks for project ID: {}", projectUuid);
        validateProjectUuid(projectUuid);
        List<Task> tasks = taskRepository.findByProjectUuid(projectUuid);
        log.debug("Fetched {} tasks for project {}.", tasks.size(), projectUuid);
        return tasks;
    }

    @Override
    public Task createTask(Task task) {
        log.debug("Creating new task: {}", task);
        validateTask(task);
        task.setUuid(UUID.randomUUID().toString());
        Task savedTask = taskRepository.save(task);
        log.info("New task created with title: {}", savedTask.getTitle());
        return savedTask;
    }

    @Override
    public Task getTask(String uuid) {
        log.debug("Fetching task with ID: {}", uuid);
        validateUuid(uuid);
        Task task = taskRepository.findById(uuid).orElseThrow(() -> {
            log.warn("Task with ID: {} not found.", uuid);
            return new ResourceNotFoundException("Task not found with UUID: " + uuid);
        });
        log.info("Task found: {}", task);
        return task;
    }

    @Override
    public Task updateTask(String uuid, Task task) {
        log.debug("Updating task with ID: {}", uuid);
        validateUuid(uuid);
        validateTask(task);
        getTask(uuid);
        task.setUuid(uuid);
        Task updatedTask = taskRepository.update(task);
        log.info("Task updated with title: {}", updatedTask.getTitle());
        return updatedTask;
    }

    @Override
    public void deleteTask(String uuid) {
        log.debug("Deleting task with ID: {}", uuid);
        validateUuid(uuid);
        Task task = getTask(uuid);
        try {
            taskRepository.deleteById(uuid);
            log.info("Task deleted with title: {}", task.getTitle());
        } catch (Exception e) {
            log.error("Error occurred while deleting task with ID: {}", uuid, e);
            throw new ConflictException("Error occurred while deleting task: " + e.getMessage(), e);
        }
    }

    @Override
    public void validateTask(Task task) {
        if (task == null) {
            log.warn("Attempt to create or update a null task.");
            throw new InvalidArgumentException("Task is required");
        }
        if (task.getProjectUuid() == null || task.getProjectUuid().trim().isEmpty()) {
            log.warn("Attempt to create/update a task without a project UUID.");
            throw new InvalidArgumentException("Task project UUID is required");
        }
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            log.warn("Attempt to create/update a task with an empty title.");
            throw new InvalidArgumentException("Task title is required and cannot be empty");
        }
        if (task.getStartDate() == null) {
            log.warn("Attempt to create/update a task with a null start date.");
            throw new InvalidArgumentException("Task start date is required");
        }
        if (task.getDurationWeeks() <= 0) {
            log.warn("Attempt to create/update a task with non-positive duration.");
            throw new InvalidArgumentException("Task duration must be greater than 0");
        }
    }

    private void validateUuid(String uuid) {
        if (uuid == null || uuid.trim().isEmpty()) {
            log.warn("Attempt to use an empty task UUID.");
            throw new InvalidArgumentException("Task UUID is required");
        }
    }

    private void validateProjectUuid(String projectUuid) {
        if (projectUuid == null || projectUuid.trim().isEmpty()) {
            log.warn("Attempt to use an empty project UUID for task operations.");
            throw new InvalidArgumentException("Project UUID is required");
        }
    }
}
