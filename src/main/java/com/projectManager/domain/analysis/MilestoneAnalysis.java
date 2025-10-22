package com.projectManager.domain.analysis;

import com.projectManager.domain.dateType.DateType;
import com.projectManager.domain.project.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneAnalysis {
    private String milestoneUuid;
    private String milestoneTitle;
    @NonNull
    private DateType startDate;
    @NonNull private DateType endDate;
    @NonNull private Long initialCompletion;
    @NonNull private Long endCompletion;
    @NonNull private ArrayList<TaskAnalysis> tasklist;
}