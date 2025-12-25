package com.project_manager.business.analysis.port;

import com.project_manager.business.analysis.model.ProjectAnalysis;

import java.util.List;

public interface AnalysisRepository {
    ProjectAnalysis save(ProjectAnalysis analysis);
    List<ProjectAnalysis> findAllByProjectUuid(String projectUuid);
    void deleteByProjectUuid(String projectUuid);
}
