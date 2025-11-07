package com.projectManager.adapter.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PROJECT")
public class ProjectEntity {
    @Id
    @Column(nullable = false)
    private String uuid;
    
    @Column(nullable = false)
    private String title;
    
    private String description;
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    
    @ElementCollection
    @CollectionTable(name = "project_additional_fields", joinColumns = @JoinColumn(name = "project_uuid"))
    @Column(name = "additional_field_value")
    private Map<String, String> additionalFields;
}
