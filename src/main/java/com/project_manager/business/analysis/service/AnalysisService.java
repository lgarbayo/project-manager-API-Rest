package com.project_manager.business.analysis.service;

import com.project_manager.business.analysis.model.ProjectAnalysis;
import com.project_manager.business.project.model.Milestone;
import com.project_manager.business.project.model.Task;
import com.project_manager.shared.core.project.ProjectCoreData;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface AnalysisService {
    ProjectAnalysis analyzeProject(ProjectCoreData projectCoreData, List<Milestone> milestones, List<Task> tasks);
}
