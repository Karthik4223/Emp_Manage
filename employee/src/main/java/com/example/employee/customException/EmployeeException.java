package com.example.employee.customException;


public class EmployeeException extends Exception {

    private final String customMessage;

    public EmployeeException(String message) {
        super(message);
        this.customMessage = message;
    }

    public EmployeeException(Throwable cause) {
        super(cause);
        this.customMessage = cause.getMessage();
    }

    public EmployeeException(String message, Throwable cause) {
        super(message, cause);
        this.customMessage = message;
    }
    
    @Override
    public String getMessage() {
        return customMessage;
    }
    
    @Override
    public String toString() {
    	return customMessage;
    }

}
