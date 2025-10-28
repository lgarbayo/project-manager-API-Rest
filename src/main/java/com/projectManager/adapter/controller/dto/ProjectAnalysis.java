package com.projectManager.adapter.controller.dto;

import com.projectManager.domain.project.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAnalysis {
    @NonNull private Project project;
    @NonNull private ArrayList<MilestoneAnalysis> milestoneList;
}
