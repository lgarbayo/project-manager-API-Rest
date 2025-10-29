package com.projectManager.domain.task;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {
    List<Task> listTasks();
    Task createTask(Task task);
    Task getTask(String uuid);
    Task updateTask(String uuid, Task task);
    void deleteTask(String uuid);
    void validateTask(Task task);
}
