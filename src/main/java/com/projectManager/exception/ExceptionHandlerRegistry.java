package com.projectManager.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ExceptionHandlerRegistry {

    private final Map<Class<? extends ManagerException>, ExceptionHandlerCommand> exceptionHandler = new HashMap<>();

    public ExceptionHandlerRegistry() {
        registerHandlers();
    }

    private void registerHandlers() {
        exceptionHandler.put(InvalidArgumentException.class, (ex, request) -> {
            return buildResponse(HttpStatus.BAD_REQUEST, "Invalid Argument", ex, request);
        });
        exceptionHandler.put(ResourceNotFoundException.class, (ex, request) -> {
            return buildResponse(HttpStatus.NOT_FOUND, "Resource Not Found", ex, request);
        });
        exceptionHandler.put(ConflictException.class, (ex, request) -> {
            return buildResponse(HttpStatus.CONFLICT, "Conflict", ex, request);
        });
    }

    public ResponseEntity<ErrorResponse> executeCommand(ManagerException ex, WebRequest request) {
        ExceptionHandlerCommand command = exceptionHandler.get(ex.getClass());

        if (command == null) {
            log.error("No handler registered for exception type: {}", ex.getClass().getSimpleName());
            return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex, request);
        }

        return command.handle(ex, request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status, String error, ManagerException ex, WebRequest request) {

        String requestPath = request.getDescription(false).replace("uri=", "");

        log.warn("{} on {}: {}", ex.getClass().getSimpleName(), requestPath, ex.getMessage());
        log.debug("Exception details", ex);

        ErrorResponse response = new ErrorResponse(
                status.value(),
                error,
                ex.getMessage(),
                requestPath,
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(response);

    }
}
