package com.projectManager.exception;

public class InvalidArgumentException extends ManagerException {
    
    private static final long serialVersionUID = -5400412368947993168L;

    public InvalidArgumentException() {
        super();
    }

    public InvalidArgumentException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidArgumentException(String message) {
        super(message);
    }

    public InvalidArgumentException(Throwable cause) {
        super(cause);
    }

}
