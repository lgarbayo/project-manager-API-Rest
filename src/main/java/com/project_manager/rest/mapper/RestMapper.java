package com.project_manager.rest.mapper;

import com.project_manager.business.analysis.model.MilestoneAnalysis;
import com.project_manager.business.analysis.model.ProjectAnalysis;
import com.project_manager.business.analysis.model.TaskAnalysis;
import com.project_manager.business.analysis.model.TaskDescriptionProposal;
import com.project_manager.business.analysis.model.TaskEstimation;
import com.project_manager.business.project.model.Milestone;
import com.project_manager.business.project.model.Project;
import com.project_manager.business.project.model.Task;
import com.project_manager.rest.command.UpsertMilestoneCommand;
import com.project_manager.rest.command.UpsertProjectCommand;
import com.project_manager.rest.command.UpsertTaskCommand;
import com.project_manager.rest.response.*;
import com.project_manager.shared.core.project.ProjectCoreData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestMapper {
    // project
    ProjectResponse toProjectResponse(Project project);
    ProjectResponse toProjectResponseFromCoreData(ProjectCoreData projectCoreData);
    List<ProjectResponse> toProjectResponseList(List<Project> projects);
    Project toProject(UpsertProjectCommand command);
    // milestone
    MilestoneResponse toMilestoneResponse(Milestone milestone);
    List<MilestoneResponse> toMilestoneResponseList(List<Milestone> milestones);
    @Mapping(target = "projectUuid", source = "projectUuid")
    Milestone toMilestone(UpsertMilestoneCommand command, String projectUuid);
    // task
    TaskResponse toTaskResponse(Task task);
    List<TaskResponse> toTaskResponseList(List<Task> tasks);
    @Mapping(target = "projectUuid", source = "projectUuid")
    Task toTask(UpsertTaskCommand command, String projectUuid);
    // analysis
    ProjectAnalysisResponse toProjectAnalysisResponse(ProjectAnalysis analysis);
    MilestoneAnalysisResponse toMilestoneAnalysisResponse(MilestoneAnalysis analysis);
    TaskAnalysisResponse toTaskAnalysisResponse(TaskAnalysis analysis);
    List<MilestoneAnalysisResponse> toMilestoneAnalysisResponseList(List<MilestoneAnalysis> analyses);
    List<TaskAnalysisResponse> toTaskAnalysisResponseList(List<TaskAnalysis> analyses);
    TaskEstimationResponse toTaskEstimationResponse(TaskEstimation estimation);
    TaskDescriptionResponse toTaskDescriptionResponse(TaskDescriptionProposal proposal);
}
