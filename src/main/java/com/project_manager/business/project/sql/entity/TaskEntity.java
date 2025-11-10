package com.project_manager.business.project.persistence.sql.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TASK")
public class TaskEntity {
    @Id
    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String projectUuid;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private int durationWeeks;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
}
