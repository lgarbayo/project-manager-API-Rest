package com.projectManager.domain.facade;

import com.projectManager.core.project.ProjectCoreData;
import com.projectManager.domain.project.Milestone;
import com.projectManager.domain.project.Task;

import java.util.List;

public interface ProjectFacade {
    void checkDependencies(String projectUuid);
    
    // New methods for Analysis domain support
    ProjectCoreData getProjectCoreData(String projectUuid);
    List<Milestone> getMilestonesByProject(String projectUuid);
    List<Task> getTasksByProject(String projectUuid);
}
