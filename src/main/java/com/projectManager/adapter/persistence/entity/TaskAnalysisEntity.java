package com.projectManager.adapter.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TASK_ANALYSIS")
public class TaskAnalysisEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "task_uuid", nullable = false)
    private String taskUuid;
    
    @Column(name = "task_title", nullable = false)
    private String taskTitle;
    
    @Column(name = "initial_completion")
    private Double initialCompletion;
    
    @Column(name = "end_completion")
    private Double endCompletion;
    
    @ManyToOne
    @JoinColumn(name = "milestone_analysis_id", nullable = false)
    private MilestoneAnalysisEntity milestoneAnalysis;
}
