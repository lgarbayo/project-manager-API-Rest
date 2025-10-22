package com.projectManager.domain.task;

import com.projectManager.domain.dateType.DateType;
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
    @NonNull private int durationWeeks;
    @NonNull private DateType startDate;
}