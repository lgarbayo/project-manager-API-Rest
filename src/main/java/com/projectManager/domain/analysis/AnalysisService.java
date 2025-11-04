package com.projectManager.domain.analysis;

import org.springframework.stereotype.Service;

@Service
public interface AnalysisService {
    ProjectAnalysis analyzeProject(String projectUuid);
}