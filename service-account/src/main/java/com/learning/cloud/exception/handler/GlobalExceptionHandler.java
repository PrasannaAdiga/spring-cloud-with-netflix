package com.learning.cloud.exception.handler;

import com.learning.cloud.exception.response.RestApiResponseErrorMessage;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(status.value())
                .error("Validation Errors")
                .message(ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList()).toString())
                .path(((ServletWebRequest) request).getRequest().getRequestURI().toString())
                .build();
        return ResponseEntity.badRequest().body(restApiResponseErrorMessage);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(status.value())
                .error("Type Mismatch")
                .message(ex.getMessage())
                .path(((ServletWebRequest) request).getRequest().getRequestURI().toString())
                .build();
        return ResponseEntity.badRequest().body(restApiResponseErrorMessage);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(status.value())
                .error("Invalid JSON")
                .message(ex.getMessage())
                .path(((ServletWebRequest) request).getRequest().getRequestURI().toString())
                .build();
        return ResponseEntity.badRequest().body(restApiResponseErrorMessage);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(status.value())
                .error("Malformed JSON request")
                .message(ex.getMessage())
                .path(((ServletWebRequest) request).getRequest().getRequestURI().toString())
                .build();
        return ResponseEntity.badRequest().body(restApiResponseErrorMessage);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(status.value())
                .error("Missing Parameters")
                .message(ex.getMessage())
                .path(((ServletWebRequest) request).getRequest().getRequestURI().toString())
                .build();
        return ResponseEntity.badRequest().body(restApiResponseErrorMessage);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(status.value())
                .error("Method Not Found")
                .message(ex.getMessage())
                .path(((ServletWebRequest) request).getRequest().getRequestURI().toString())
                .build();
        return ResponseEntity.badRequest().body(restApiResponseErrorMessage);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(status.value())
                .error("Method Not Allowed")
                .message(ex.getMessage())
                .path(((ServletWebRequest) request).getRequest().getRequestURI().toString())
                .build();
        return new ResponseEntity<>(restApiResponseErrorMessage, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
            ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(status.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path(((ServletWebRequest) request).getRequest().getRequestURI().toString())
                .build();
        return new ResponseEntity<>(restApiResponseErrorMessage, HttpStatus.BAD_REQUEST);
    }

}
