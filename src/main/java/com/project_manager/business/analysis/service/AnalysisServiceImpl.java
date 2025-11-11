package com.project_manager.business.analysis.service;

import com.project_manager.business.analysis.model.MilestoneAnalysis;
import com.project_manager.business.analysis.model.ProjectAnalysis;
import com.project_manager.business.analysis.model.TaskAnalysis;
import com.project_manager.business.analysis.port.AnalysisRepository;
import com.project_manager.business.facade.ProjectFacade;
import com.project_manager.business.project.model.Milestone;
import com.project_manager.business.project.model.Task;
import com.project_manager.shared.core.dateType.DateType;
import com.project_manager.shared.core.project.ProjectCoreData;
import com.project_manager.shared.exception.InvalidArgumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisServiceImpl implements AnalysisService {
    private final ProjectFacade projectFacade;
    private final AnalysisRepository analysisRepository;

    @Override
    public ProjectAnalysis analyzeProject(String projectUuid) {
        if (projectUuid == null || projectUuid.trim().isEmpty()) {
            log.warn("Attempted to analyze project with null or empty UUID: {}");
            throw new InvalidArgumentException("Project UUID cannot be null or empty");
        }
        
        log.info("Building analysis for project: {}", projectUuid);
        
        ProjectCoreData projectCoreData = projectFacade.getProjectCoreData(projectUuid);

        List<Milestone> projectMilestones = projectFacade.getMilestonesByProject(projectUuid)
                .stream()
                .sorted(this::compareMilestonesByDate)
                .collect(Collectors.toList());

        List<Task> projectTasks = projectFacade.getTasksByProject(projectUuid)
                .stream()
                .sorted(this::compareTasksByStartDate)
                .collect(Collectors.toList());

        Map<String, List<Task>> tasksByMilestone = projectMilestones.stream()
                .collect(Collectors.toMap(
                        Milestone::getUuid,
                        milestone -> new ArrayList<>(),
                        (left, right) -> left,
                        LinkedHashMap::new
                ));

        if (!projectMilestones.isEmpty()) {
            projectTasks.forEach(task -> {
                Milestone targetMilestone = resolveTargetMilestone(projectMilestones, task);
                if (targetMilestone != null) {
                    tasksByMilestone.get(targetMilestone.getUuid()).add(task);
                }
            });
        } else if (!projectTasks.isEmpty()) {
            log.debug("Project {} has tasks but no milestones to associate them with.", projectCoreData.getTitle());
        }

        LocalDate analysisDate = LocalDate.now();

        List<MilestoneAnalysis> milestoneAnalyses = projectMilestones.stream()
                .map(milestone -> {
                    List<Task> milestoneTasks = tasksByMilestone.getOrDefault(milestone.getUuid(), Collections.emptyList());
                    List<TaskAnalysis> taskAnalyses = buildTaskAnalyses(milestoneTasks, analysisDate);
                    double milestoneCompletion = calculateAverageCompletion(taskAnalyses);
                    return new MilestoneAnalysis(
                            milestone.getUuid(),
                            milestone.getTitle(),
                            milestone.getDate(),
                            milestone.getDate(),
                            milestoneCompletion,
                            milestoneCompletion,
                            taskAnalyses
                    );
                })
                .collect(Collectors.toList());

        ProjectAnalysis analysis = new ProjectAnalysis(projectCoreData, milestoneAnalyses);
        
        // save analysis to repository
        ProjectAnalysis savedAnalysis = analysisRepository.save(analysis);
        log.debug("Project analysis generated and saved for {} with {} milestones.", projectCoreData.getTitle(), milestoneAnalyses.size());
        
        return savedAnalysis;
    }

    private Milestone resolveTargetMilestone(List<Milestone> sortedMilestones, Task task) {
        if (sortedMilestones == null) {
            log.warn("Attempted to resolve target milestone with null milestone list");
            throw new InvalidArgumentException("Milestone list cannot be null");
        }
        if (task == null) {
            log.warn("Attempted to resolve target milestone with null task");
            throw new InvalidArgumentException("Task cannot be null");
        }
        if (task.getStartDate() == null) {
            log.warn("Attempted to resolve target milestone for task with null start date");
            throw new InvalidArgumentException("Task start date cannot be null");
        }
        
        LocalDate taskStart = toLocalDate(task.getStartDate());
        Milestone lastVisited = null;
        
        for (Milestone milestone : sortedMilestones) {
            if (milestone == null) {
                log.warn("Null milestone found in sorted list, skipping");
                continue;
            }
            
            LocalDate milestoneDate = toLocalDate(milestone.getDate());
            if (!milestoneDate.isBefore(taskStart)) {
                return milestone;
            }
            lastVisited = milestone;
        }
        return lastVisited;
    }

    private int compareMilestonesByDate(Milestone left, Milestone right) {
        if (left == null || right == null) {
            log.warn("Attempted to compare null milestones");
            throw new InvalidArgumentException("Cannot compare null milestones");
        }
        return toLocalDate(left.getDate()).compareTo(toLocalDate(right.getDate()));
    }

    private int compareTasksByStartDate(Task left, Task right) {
        if (left == null || right == null) {
            log.warn("Attempted to compare null tasks");
            throw new InvalidArgumentException("Cannot compare null tasks");
        }
        return toLocalDate(left.getStartDate()).compareTo(toLocalDate(right.getStartDate()));
    }

    private LocalDate toLocalDate(DateType date) {
        if (date == null) {
            log.warn("Attempted to convert null DateType to LocalDate");
            throw new InvalidArgumentException("Date cannot be null");
        }
        
        // Validar rangos de la fecha
        if (date.getYear() < 2000 || date.getYear() > 3000) {
            log.warn("Attempted to convert DateType with invalid year: {}");
            throw new InvalidArgumentException("Year must be between 2000 and 3000, but was: " + date.getYear());
        }
        
        if (date.getMonth() < 0 || date.getMonth() > 11) {
            log.warn("Attempted to convert DateType with invalid month: {}");
            throw new InvalidArgumentException("Month must be between 0-11 (0=January, 11=December), but was: " + date.getMonth());
        }
        
        if (date.getWeek() < 0 || date.getWeek() > 3) {
            log.warn("Attempted to convert DateType with invalid week: {}");
            throw new InvalidArgumentException("Week must be between 0-3 within the month, but was: " + date.getWeek());
        }
        
        try {
            int month = date.getMonth() + 1;
            LocalDate firstDayOfMonth = LocalDate.of(date.getYear(), month, 1);
            LocalDate resultDate = firstDayOfMonth.plusWeeks(date.getWeek());
            
            if (resultDate.getMonthValue() != month) {
                log.warn("Date calculation resulted in different month. Original: {}-{}, Calculated: {}-{}", 
                    date.getYear(), month, resultDate.getYear(), resultDate.getMonthValue());
            }
            
            return resultDate;
        } catch (Exception e) {
            throw new InvalidArgumentException("Invalid date: year=" + date.getYear() + 
                ", month=" + date.getMonth() + ", week=" + date.getWeek(), e);
        }
    }

    private List<TaskAnalysis> buildTaskAnalyses(List<Task> tasks, LocalDate analysisDate) {
        if (tasks == null || tasks.isEmpty()) {
            return Collections.emptyList();
        }
        return tasks.stream()
                .map(task -> {
                    double completion = calculateTaskCompletion(task, analysisDate);
                    return new TaskAnalysis(task.getUuid(), task.getTitle(), completion, completion);
                })
                .collect(Collectors.toList());
    }

    private double calculateTaskCompletion(Task task, LocalDate analysisDate) {
        if (task == null) {
            return 0D;
        }
        LocalDate startDate = toLocalDate(task.getStartDate());
        LocalDate endDate = startDate.plusWeeks(Math.max(1, task.getDurationWeeks()));
        // plusWeeks adds the specified number of weeks to the start date, we add at least 1 week to avoid zero-duration tasks

        if (analysisDate.isBefore(startDate)) {
            return 0D;
        }
        if (!analysisDate.isBefore(endDate)) { // when analysisDate is on or after endDate
            return 1D;
        }

        long totalDays = Math.max(1, ChronoUnit.DAYS.between(startDate, endDate));
        long elapsedDays = Math.max(0, ChronoUnit.DAYS.between(startDate, analysisDate));

        double completion = (double) elapsedDays / totalDays;
        return Math.max(0D, Math.min(1D, completion));
    }

    private double calculateAverageCompletion(List<TaskAnalysis> taskAnalyses) {
        if (taskAnalyses == null || taskAnalyses.isEmpty()) {
            return 0D;
        }
        double sum = taskAnalyses.stream()
                .mapToDouble(TaskAnalysis::getEndCompletion)
                .sum();
        return sum / taskAnalyses.size();
    }
}
