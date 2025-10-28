package com.projectManager.domain.task;

import com.projectManager.exception.ResourceConflictException;
import com.projectManager.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        log.debug("Found {} tasks", tasks.size());
        return tasks;
    }

    @Override
    public void createTask(Task task) {
        String rawUuid = task.getUuid();
        if (rawUuid == null || rawUuid.isBlank()) {
            rawUuid = UUID.randomUUID().toString();
            task.setUuid(rawUuid);
        }

        final String uuid = rawUuid;
        taskRepository.findById(uuid).ifPresent(existing -> {
            throw new ResourceConflictException("Task with uuid %s already exists".formatted(uuid));
        });

        taskRepository.save(task);
        log.info("Created task {} ({})", uuid, task.getTitle());
    }

    @Override
    public Task getTask(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with uuid %s not found".formatted(id)));
    }

    @Override
    public void updateTask(Task task) {
        String uuid = task.getUuid();
        if (uuid == null || uuid.isBlank()) {
            throw new IllegalArgumentException("Task uuid is required for update");
        }
        getTask(uuid);
        taskRepository.update(task);
        log.info("Updated task {}", uuid);
    }

    @Override
    public void deleteTask(String id) {
        getTask(id);
        taskRepository.deleteById(id);
        log.info("Deleted task {}", id);
    }
}
