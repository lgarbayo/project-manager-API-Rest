package com.projectManager.exception;

public class ConflictException extends ManagerException {
    
    private static final long serialVersionUID = 1L;
    
    public ConflictException() {
    }
    
    public ConflictException(String message) {
        super(message);
    }
    
    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ConflictException(Throwable cause) {
        super(cause);
    }
    
    public ConflictException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
