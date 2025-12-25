package com.project_manager.persistence.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MILESTONE")
public class MilestoneEntity {
    @Id
    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String projectUuid;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate date;

    private String description;
}
