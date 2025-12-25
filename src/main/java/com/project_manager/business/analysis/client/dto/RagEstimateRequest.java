package com.project_manager.business.analysis.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RagEstimateRequest {
    @JsonProperty("project_uuid")
    private String projectUuid;
    @JsonProperty("task_uuid")
    private String taskUuid;
    private String prompt;
}
