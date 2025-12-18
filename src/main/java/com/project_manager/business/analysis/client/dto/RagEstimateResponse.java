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
public class RagEstimateResponse {
    private Integer hours;
    private String explanation;
    @JsonProperty("raw_answer")
    private String rawAnswer;
}
