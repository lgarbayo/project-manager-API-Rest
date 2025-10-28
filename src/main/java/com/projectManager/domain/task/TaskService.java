package com.projectManager.domain.task;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {
    List<Task> listTasks();
    void createTask(Task task);
    Task getTask(String id);
    void updateTask(Task task);
    void deleteTask(String id);
}
