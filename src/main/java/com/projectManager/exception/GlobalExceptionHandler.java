package com.projectManager.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final ExceptionHandlerRegistry exceptionHandlerRegistry;
    
    public GlobalExceptionHandler(ExceptionHandlerRegistry exceptionHandlerRegistry) {
        this.exceptionHandlerRegistry = exceptionHandlerRegistry;
    }
    
    @ExceptionHandler(ManagerException.class)
    public ResponseEntity<ErrorResponse> handleException(ManagerException ex, WebRequest request) {
        log.info("Handling exception: {} for request: {}", ex.getClass().getSimpleName(), request.getDescription(false));
        return exceptionHandlerRegistry.executeCommand(ex, request);
    }
    
}
