package com.learning.cloud.exception.handler;

import com.learning.cloud.exception.ResourceFoundException;
import com.learning.cloud.exception.ResourceNotFoundException;
import com.learning.cloud.exception.message.RestApiErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomAPIExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RestApiErrorMessage> handleConstraintViolationException(final Exception exception, final HttpServletRequest request) {
        RestApiErrorMessage restApiErrorMessage = RestApiErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.badRequest().body(restApiErrorMessage);
    }

    @ExceptionHandler(ResourceFoundException.class)
    public ResponseEntity<RestApiErrorMessage> handleResourceFoundException(final Exception exception, final HttpServletRequest request) {
        RestApiErrorMessage restApiErrorMessage = RestApiErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.FOUND.value())
                .error("Resource already exists!")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(restApiErrorMessage, HttpStatus.FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RestApiErrorMessage> handleResourceNotFoundException(final Exception exception, final HttpServletRequest request) {
        RestApiErrorMessage restApiErrorMessage = RestApiErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Resource not found!")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(restApiErrorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<RestApiErrorMessage> handleNullPointerException(final Exception exception, final HttpServletRequest request) {
        RestApiErrorMessage restApiErrorMessage = RestApiErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Null pointer com.learning.cloud.exception")
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(restApiErrorMessage, HttpStatus.NOT_FOUND);
    }
}
