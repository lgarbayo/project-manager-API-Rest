package com.projectManager.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

@FunctionalInterface
public interface ExceptionHandlerCommand {
    ResponseEntity<ErrorResponse> handle(ManagerException ex, WebRequest request);
}
