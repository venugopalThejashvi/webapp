package com.neu.cloud.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        printExceptionLog(ex.getMessage(),request.getRequestURI());
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(CannotCreateTransactionException.class)
    public ResponseEntity<?> databaseException(CannotCreateTransactionException ex, HttpServletRequest request){
        printExceptionLog(ex.getMessage(),request.getRequestURI());
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> resourceNotException(NoResourceFoundException ex, HttpServletRequest request){
        printExceptionLog(ex.getMessage(),request.getRequestURI());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> genericException(Exception ex, HttpServletRequest request){
        printExceptionLog(ex.getMessage(),request.getRequestURI());
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> responseStatusException(ResponseStatusException ex, HttpServletRequest request){
        printExceptionLog(ex.getMessage(),request.getRequestURI());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> responseStatusException(HttpMediaTypeNotSupportedException ex, HttpServletRequest request){
        printExceptionLog(ex.getMessage(),request.getRequestURI());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private void printExceptionLog(String errorMessage, String uri){
        log.error(uri+" "+errorMessage);
    }
}
