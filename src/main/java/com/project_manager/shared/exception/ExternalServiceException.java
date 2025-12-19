package com.project_manager.shared.exception;

public class ExternalServiceException extends ManagerException {

    private static final long serialVersionUID = 1L;

    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
