package com.projectManager.adapter.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAnalysisResponse {
    private ProjectResponse project;
    private ArrayList<MilestoneAnalysisResponse> milestoneList;
}