package com.projectManager.domain.project;

import com.projectManager.core.dateType.DateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private String uuid;
    private String projectUuid;
    @NonNull private String title;
    private String description;
    private int durationWeeks;
    @NonNull private DateType startDate;
}