package com.project_manager.business.analysis.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.project_manager.business.analysis.client.dto.RagEstimateRequest;
import com.project_manager.business.analysis.client.dto.RagEstimateResponse;
import com.project_manager.business.analysis.client.dto.RagDescriptionResponse;
import com.project_manager.business.analysis.model.TaskDescriptionProposal;
import com.project_manager.business.analysis.model.TaskEstimation;
import com.project_manager.shared.exception.ManagerException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OpenApiRagClient implements TaskEstimatorClient, TaskDescriptionClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String estimatePathTemplate;
    private final String descriptionPathTemplate;
    private final String apiKey;

    public OpenApiRagClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${rag.client.base-url:http://fastapi:8000}") String baseUrl,
            @Value("${rag.client.estimate-path:/project/%s/task/%s/estimate}") String estimatePathTemplate,
            @Value("${rag.client.description-path:/project/%s/task/%s/description}") String descriptionPathTemplate,
            @Value("${rag.client.api-key:}") String apiKey,
            @Value("${rag.client.connect-timeout-ms:5000}") long connectTimeout,
            @Value("${rag.client.read-timeout-ms:60000}") long readTimeout
    ) {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(connectTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .build();
        this.baseUrl = sanitizeBaseUrl(baseUrl);
        this.estimatePathTemplate = sanitizePath(estimatePathTemplate);
        this.descriptionPathTemplate = sanitizePath(descriptionPathTemplate);
        this.apiKey = apiKey;
    }

    @Override
    public TaskEstimation estimateTask(String projectUuid, String taskUuid, String prompt) {
        if (!StringUtils.hasText(prompt)) {
            throw new ManagerException("Prompt is required for RAG estimation");
        }

        RagEstimateRequest request = RagEstimateRequest.builder()
                .projectUuid(projectUuid)
                .taskUuid(taskUuid)
                .prompt(prompt)
                .build();

        RagEstimateResponse response = invokeEstimate(request);
        return mapToTaskEstimation(projectUuid, taskUuid, prompt, response);
    }

    @Override
    public TaskDescriptionProposal describeTask(String projectUuid, String taskUuid, String prompt) {
        if (!StringUtils.hasText(prompt)) {
            throw new ManagerException("Prompt is required for RAG description");
        }

        RagEstimateRequest request = RagEstimateRequest.builder()
                .projectUuid(projectUuid)
                .taskUuid(taskUuid)
                .prompt(prompt)
                .build();

        RagDescriptionResponse response = invokeDescription(request);
        return mapToTaskDescription(projectUuid, taskUuid, prompt, response);
    }

    private RagEstimateResponse invokeEstimate(RagEstimateRequest request) {
        String url = buildUrl(estimatePathTemplate, request.getProjectUuid(), request.getTaskUuid());
        HttpHeaders headers = buildHeaders();
        HttpEntity<RagEstimateRequest> entity = new HttpEntity<>(request, headers);
        try {
            log.debug("Calling RAG estimator {} for task {}:{}", url, request.getProjectUuid(), request.getTaskUuid());
            return restTemplate.postForObject(url, entity, RagEstimateResponse.class);
        } catch (RestClientException ex) {
            log.error("Failed to call RAG estimator at {}: {}", url, ex.getMessage());
            throw new ManagerException("Unable to retrieve estimation from external service", ex);
        }
    }

    private RagDescriptionResponse invokeDescription(RagEstimateRequest request) {
        String url = buildUrl(descriptionPathTemplate, request.getProjectUuid(), request.getTaskUuid());
        HttpHeaders headers = buildHeaders();
        HttpEntity<RagEstimateRequest> entity = new HttpEntity<>(request, headers);
        try {
            log.debug("Calling RAG description {} for task {}:{}", url, request.getProjectUuid(), request.getTaskUuid());
            return restTemplate.postForObject(url, entity, RagDescriptionResponse.class);
        } catch (RestClientException ex) {
            log.error("Failed to call RAG description at {}: {}", url, ex.getMessage());
            throw new ManagerException("Unable to retrieve description from external service", ex);
        }
    }

    private TaskEstimation mapToTaskEstimation(String projectUuid, String taskUuid, String prompt, RagEstimateResponse response) {
        return TaskEstimation.builder()
                .projectUuid(projectUuid)
                .taskUuid(taskUuid)
                .prompt(prompt)
                .hours(response != null ? response.getHours() : null)
                .explanation(response != null ? response.getExplanation() : null)
                .rawAnswer(response != null ? response.getRawAnswer() : null)
                .build();
    }

    private TaskDescriptionProposal mapToTaskDescription(String projectUuid, String taskUuid, String prompt,
            RagDescriptionResponse response) {
        return TaskDescriptionProposal.builder()
                .projectUuid(projectUuid)
                .taskUuid(taskUuid)
                .prompt(prompt)
                .title(response != null ? response.getTitle() : null)
                .description(response != null ? response.getDescription() : null)
                .rawAnswer(response != null ? response.getRawAnswer() : null)
                .build();
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (StringUtils.hasText(apiKey)) {
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey.trim());
        }
        return headers;
    }

    private String sanitizeBaseUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return "http://fastapi:8000";
        }
        String sanitized = url.trim();
        if (sanitized.endsWith("/")) {
            sanitized = sanitized.substring(0, sanitized.length() - 1);
        }
        return sanitized;
    }

    private String sanitizePath(String path) {
        if (!StringUtils.hasText(path)) {
            return "/project/%s/task/%s/estimate";
        }
        String sanitized = path.trim();
        if (!sanitized.startsWith("/")) {
            sanitized = "/" + sanitized;
        }
        return sanitized;
    }

    private String buildUrl(String template, String projectUuid, String taskUuid) {
        if (!sanitizeId(projectUuid)) {
            throw new ManagerException("Project UUID is required to build the RAG estimate URL");
        }
        if (!sanitizeId(taskUuid)) {
            throw new ManagerException("Task UUID is required to build the RAG estimate URL");
        }
        try {
            String formattedPath = String.format(template, projectUuid, taskUuid);
            return baseUrl + formattedPath;
        } catch (IllegalArgumentException ex) {
            log.error("Invalid RAG path template {} - {}", template, ex.getMessage());
            throw new ManagerException("Invalid RAG estimate path template", ex);
        }
    }

    private boolean sanitizeId(String value) {
        return StringUtils.hasText(value);
    }
}
