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
import com.project_manager.shared.exception.ManagerException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OpenApiRagClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String estimatePathTemplate;
    private final String apiKey;

    public OpenApiRagClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${rag.client.base-url:http://fastapi:8000}") String baseUrl,
            @Value("${rag.client.estimate-path:/project/%s/task/%s/estimate}") String estimatePathTemplate,
            @Value("${rag.client.api-key:}") String apiKey,
            @Value("${rag.client.connect-timeout-ms:2000}") long connectTimeout,
            @Value("${rag.client.read-timeout-ms:10000}") long readTimeout
    ) {
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(connectTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .build();
        this.baseUrl = sanitizeBaseUrl(baseUrl);
        this.estimatePathTemplate = sanitizePath(estimatePathTemplate);
        this.apiKey = apiKey;
    }

    public RagEstimateResponse estimateTask(RagEstimateRequest request) {
        if (request == null) {
            throw new ManagerException("RAG estimate request cannot be null");
        }

        String url = buildEstimateUrl(request.getProjectUuid(), request.getTaskUuid());
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

    private String buildEstimateUrl(String projectUuid, String taskUuid) {
        if (!sanitizeId(projectUuid)) {
            throw new ManagerException("Project UUID is required to build the RAG estimate URL");
        }
        if (!sanitizeId(taskUuid)) {
            throw new ManagerException("Task UUID is required to build the RAG estimate URL");
        }
        try {
            String formattedPath = String.format(estimatePathTemplate, projectUuid, taskUuid);
            return baseUrl + formattedPath;
        } catch (IllegalArgumentException ex) {
            log.error("Invalid RAG path template {} - {}", estimatePathTemplate, ex.getMessage());
            throw new ManagerException("Invalid RAG estimate path template", ex);
        }
    }

    private boolean sanitizeId(String value) {
        return StringUtils.hasText(value);
    }
}
