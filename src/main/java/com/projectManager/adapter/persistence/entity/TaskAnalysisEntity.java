package com.projectManager.adapter.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TaskAnalysisEntity {
    @Column(name = "task_uuid", nullable = false)
    private String taskUuid;
    
    @Column(name = "task_title", nullable = false)
    private String taskTitle;
    
    @Column(name = "initial_completion")
    private Double initialCompletion;
    
    @Column(name = "end_completion")
    private Double endCompletion;
}
