package com.project_manager.rest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDescriptionResponse {
    private String projectUuid;
    private String taskUuid;
    private String prompt;
    private String title;
    private String description;
    private String rawAnswer;
}
