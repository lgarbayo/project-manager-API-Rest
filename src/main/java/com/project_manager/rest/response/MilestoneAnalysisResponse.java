package com.project_manager.rest.response;

import com.project_manager.shared.core.dateType.DateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneAnalysisResponse {
    private String milestoneUuid;
    private String milestoneTitle;
    private DateType startDate;
    private DateType endDate;
    private double initialCompletion;
    private double endCompletion;
    private List<TaskAnalysisResponse> taskList;
}