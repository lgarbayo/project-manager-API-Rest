package com.project_manager.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project_manager.rest.mapper.RestMapper;
import com.project_manager.rest.response.ProjectAnalysisResponse;
import com.project_manager.business.facade.ProjectFacade;
import com.project_manager.business.analysis.model.ProjectAnalysis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Slf4j
public class AnalysisController {
    private final ProjectFacade projectFacade;
    private final RestMapper restMapper;

    @GetMapping("/{projectUuid}/analysis")
    public ProjectAnalysisResponse analyzeProject(@PathVariable String projectUuid) {
        log.debug("GET /project/{}/analysis - Building analysis", projectUuid);
        ProjectAnalysis analysis = projectFacade.analyzeProject(projectUuid);
        log.info("Analysis completed successfully for project: {}", projectUuid);
        return restMapper.toProjectAnalysisResponse(analysis);
    }
}
