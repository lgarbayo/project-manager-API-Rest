package com.project_manager.persistence.analysis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PROJECT_ANALYSIS")
public class ProjectAnalysisEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ideal for analysis records to use an intern auto-increment ID 

    // atributos de ProjectCoreData
    @Column(name = "project_uuid", nullable = false)
    private String projectUuid; 
    
    @Column(name = "project_title", nullable = false)
    private String projectTitle;
    
    @Column(name = "project_description")
    private String projectDescription;
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ElementCollection
    @CollectionTable(name = "project_additional_fields", joinColumns = @JoinColumn(name = "project_uuid"))
    @MapKeyColumn(name = "field_key")
    @Column(name = "field_value")
    private Map<String, String> additionalFields;
    
    @OneToMany(mappedBy = "projectAnalysis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MilestoneAnalysisEntity> milestoneAnalyses;
}
