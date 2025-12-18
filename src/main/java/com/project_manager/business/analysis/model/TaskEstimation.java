package com.project_manager.business.analysis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEstimation {
    private String projectUuid;
    private String taskUuid;
    private String prompt;
    private Integer hours;
    private String explanation;
    private String rawAnswer;
}
