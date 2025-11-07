package com.projectManager.domain.analysis;

import com.projectManager.core.dateType.DateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneAnalysis {
    @NonNull private String milestoneUuid;
    @NonNull private String milestoneTitle;
    @NonNull private DateType startDate;
    @NonNull private DateType endDate;
    private double initialCompletion; // 0-1 -> percentage
    private double endCompletion; // 0-1 -> percentage
    @NonNull private List<TaskAnalysis> taskList;
}