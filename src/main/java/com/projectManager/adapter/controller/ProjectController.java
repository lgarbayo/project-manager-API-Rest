package com.projectManager.adapter.controller;

import com.projectManager.domain.project.Project;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest projectRequest) {
        log.info("Creating project with title: {}", projectRequest.getTitle());
        Project project = projectService.createProject(projectRequest.toDomain());
        return new ResponseEntity<>(ProjectResponse.fromDomain(project), HttpStatus.CREATED);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable String uuid) {
        log.info("Fetching project with UUID: {}", uuid);
        Project project = projectService.getProjectByUuid(uuid);
        return new ResponseEntity<>(ProjectResponse.fromDomain(project), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        log.info("Fetching all projects");
        List<Project> projects = projectService.getAllProjects();
        List<ProjectResponse> responseList = projects.stream()
                .map(ProjectResponse::fromDomain)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable String uuid, @RequestBody ProjectRequest projectRequest) {
        log.info("Updating project with UUID: {}", uuid);
        Project updatedProject = projectService.updateProject(uuid, projectRequest.toDomain());
        return new ResponseEntity<>(ProjectResponse.fromDomain(updatedProject), HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteProject(@PathVariable String uuid) {
        log.info("Deleting project with UUID: {}", uuid);
        projectService.deleteProject(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}