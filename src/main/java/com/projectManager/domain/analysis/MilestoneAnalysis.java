package com.projectManager.domain.analysis;

import com.projectManager.domain.dateType.DateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneAnalysis {
    @NonNull private String milestoneUuid;
    @NonNull private String milestoneTitle;
    @NonNull private DateType startDate;
    @NonNull private DateType endDate;
    private double initialCompletion;
    private double endCompletion;
    @NonNull private ArrayList<TaskAnalysis> taskList;
}