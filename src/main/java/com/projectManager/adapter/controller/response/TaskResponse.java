package com.projectManager.adapter.controller.response;

import com.projectManager.core.dateType.DateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private String uuid;
    private String projectUuid;
    private String title;
    private String description;
    private int durationWeeks;
    private DateType startDate;
}