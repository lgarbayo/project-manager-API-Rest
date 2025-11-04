package com.projectManager.domain.analysis;

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
    private double initialCompletion;
    private double endCompletion;
}