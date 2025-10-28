package com.projectManager.adapter.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAnalysis {
    @NonNull private String taskUuid;
    @NonNull private String taskTitle;
    @NonNull private Long initialCompletion;
    @NonNull private Long endCompletion;
}
