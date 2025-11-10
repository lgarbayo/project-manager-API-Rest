package com.project_manager.business.analysis.service;

import com.project_manager.business.analysis.model.ProjectAnalysis;
import org.springframework.stereotype.Service;

@Service
public interface AnalysisService {
    ProjectAnalysis analyzeProject(String projectUuid);
}
