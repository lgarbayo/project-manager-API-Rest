package com.projectManager.adapter.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAnalysisResponse {
    private String taskUuid;
    private String taskTitle;
    private double initialCompletion;
    private double endCompletion;
}