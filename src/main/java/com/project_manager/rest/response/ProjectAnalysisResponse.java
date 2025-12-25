package com.project_manager.rest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAnalysisResponse {
    private ProjectResponse project;
    private List<MilestoneAnalysisResponse> milestoneList;
}