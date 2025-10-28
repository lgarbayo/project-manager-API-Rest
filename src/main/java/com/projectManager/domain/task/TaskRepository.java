package com.projectManager.domain.task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    List<Task> findAll();
    List<Task> findByProjectUuid(String projectUuid);
    Task save(Task task);
    Optional<Task> findById(String id);
    Task update(Task task);
    void deleteById(String id);
}
