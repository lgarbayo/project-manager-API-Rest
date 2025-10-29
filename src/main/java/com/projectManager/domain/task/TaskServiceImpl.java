package com.projectManager.domain.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.projectManager.exception.InvalidArgumentException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepository;
    
    @Override
    public List<Task> listTasks() {
        log.debug("Fetching all tasks. Total count: {}", taskRepository.findAll().size());
        return taskRepository.findAll();
    }

    @Override
    public Task createTask(Task task) {
        log.debug("Creating new task: {}", task);
        validateTask(task);
        task.setUuid(UUID.randomUUID().toString());
        log.info("New task created with title: {}", task.getTitle());
        return taskRepository.save(task);
    }

    @Override
    public Task getTask(String uuid) {
        log.debug("Fetching task with ID: {}", uuid);
        Task task = taskRepository.findById(uuid).orElse(null);
        if (task == null) {
            log.warn("Task with ID: {} not found.", uuid);
            throw new InvalidArgumentException("Task not found");
        }
        log.info("Task found: {}", task);
        return task;
    }

    @Override
    public Task updateTask(String uuid, Task task) {
        log.debug("Updating task with ID: {}", uuid);
        validateTask(task);
        task.setUuid(uuid);
        log.info("Task updated with title: {}", task.getTitle());
        taskRepository.update(task);
        return task;
    }

    @Override
    public void deleteTask(String uuid) {
        log.debug("Deleting task with ID: {}", uuid);
        Task task = taskRepository.findById(uuid).orElse(null);
        if (task == null) {
            log.warn("Task with ID: {} not found. Deletion aborted.", uuid);
            throw new InvalidArgumentException("Task not found");
        }
        log.info("Task deleted with title: {}", task.getTitle());
        taskRepository.deleteById(uuid);
    }

    @Override
    public void validateTask(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            log.warn("Attemp to create/update a task with an empty title.");
            throw new InvalidArgumentException("Task title is required and cannot be empty");
        }
        if (task.getStartDate() == null) {
            log.warn("Attemp to create/update a task with a null start date.");
            throw new InvalidArgumentException("Task start date is required");
        }
        if (task.getDurationWeeks() <= 0) {
            log.warn("Attemp to create/update a task with non-positive duration.");
            throw new InvalidArgumentException("Task duration must be greater than 0");
        }
    }
}