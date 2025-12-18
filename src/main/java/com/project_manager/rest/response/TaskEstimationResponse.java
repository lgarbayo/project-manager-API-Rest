package com.project_manager.rest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskEstimationResponse {
    private String projectUuid;
    private String taskUuid;
    private String prompt;
    private Integer minutes;
    private String explanation;
    private String rawAnswer;
}
