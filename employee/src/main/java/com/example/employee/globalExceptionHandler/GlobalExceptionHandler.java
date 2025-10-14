package com.example.employee.globalExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<String> handleJwtException(ExpiredJwtException exception) {
        return ResponseEntity
                .status(HttpStatus.REQUEST_TIMEOUT)
                .body(exception.getMessage());
    }
	
	@ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleRuntimeException(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }
	
	

}
