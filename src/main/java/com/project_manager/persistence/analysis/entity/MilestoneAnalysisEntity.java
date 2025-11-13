package com.project_manager.persistence.analysis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MILESTONE_ANALYSIS")
public class MilestoneAnalysisEntity { 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
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
    
    @ManyToOne
    @JoinColumn(name = "project_analysis_id", nullable = false)
    private ProjectAnalysisEntity projectAnalysis;
    
    @OneToMany(mappedBy = "milestoneAnalysis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskAnalysisEntity> taskAnalyses;
}
