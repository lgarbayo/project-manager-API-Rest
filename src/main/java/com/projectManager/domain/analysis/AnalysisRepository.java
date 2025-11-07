package com.projectManager.domain.analysis;

import java.util.List;

public interface AnalysisRepository {
    ProjectAnalysis save(ProjectAnalysis analysis);
    List<ProjectAnalysis> findAllByProjectUuid(String projectUuid);
    void deleteByProjectUuid(String projectUuid);
}
