package com.projectManager.adapter.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class MilestoneAnalysisEntity { 
    @Column(name = "milestone_uuid", nullable = false)
    private String milestoneUuid;
    
    @Column(name = "milestone_title", nullable = false)
    private String milestoneTitle;
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    
    @Column(name = "initial_completion")
    private Double initialCompletion;
    
    @Column(name = "end_completion")
    private Double endCompletion;
    
    @ElementCollection
    @CollectionTable(name = "task_analyses", joinColumns = @JoinColumn(name = "milestone_uuid"))
    private List<TaskAnalysisEntity> taskAnalyses;
}
