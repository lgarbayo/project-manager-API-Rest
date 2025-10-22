package com.projectManager.domain.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAnalysis {
    private String taskUuid;
    private String taskTitle;
    @NonNull private Long initialCompletion;
    @NonNull private Long endCompletion;
}