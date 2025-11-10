package com.projectManager.adapter.persistence.jpa.adapter;

import com.projectManager.domain.analysis.AnalysisRepository;
import com.projectManager.domain.analysis.ProjectAnalysis;
import com.projectManager.adapter.persistence.jpa.repository.ProjectAnalysisJpaRepository;
import com.projectManager.adapter.persistence.entity.ProjectAnalysisEntity;
import com.projectManager.adapter.persistence.mapper.ProjectAnalysisMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JpaAnalysisRepositoryAdapter implements AnalysisRepository {

    private final ProjectAnalysisJpaRepository jpaRepository;
    private final ProjectAnalysisMapper mapper;

    @Override
    public ProjectAnalysis save(ProjectAnalysis analysis) {
        log.debug("Saving analysis for project: {}", analysis.getProject().getUuid());
        ProjectAnalysisEntity entity = mapper.toEntity(analysis);
        if (entity == null) {
            log.error("Failed to convert milestone to entity");
            throw new IllegalArgumentException("Milestone entity conversion failed");
        }
        ProjectAnalysisEntity saved = jpaRepository.save(entity);
        ProjectAnalysis savedAnalysis = mapper.toDomain(saved);
        log.info("Analysis saved successfully for project: {}", analysis.getProject().getUuid());
        return savedAnalysis;
    }

    @Override
    public List<ProjectAnalysis> findAllByProjectUuid(String projectUuid) {
        log.debug("Finding all analyses for project: {}", projectUuid);
        List<ProjectAnalysis> analyses = jpaRepository.findAllByProjectUuid(projectUuid)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
        log.info("Found {} analyses for project: {}", analyses.size(), projectUuid);
        return analyses;
    }

    @Override
    public void deleteByProjectUuid(String projectUuid) {
        log.debug("Deleting all analyses for project: {}", projectUuid);
        jpaRepository.deleteByProjectUuid(projectUuid);
        log.info("All analyses deleted for project: {}", projectUuid);
    }

}
