package com.project_manager.business.analysis.model;

import com.project_manager.shared.core.project.ProjectCoreData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAnalysis {
    @NonNull private ProjectCoreData project;
    @NonNull private List<MilestoneAnalysis> milestoneList;
}