package com.learning.cloud.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.learning.cloud.exception.response.RestApiResponseErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@Component
public class RestAccessDeniedErrorHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("User is not Authorized")
                .message(accessDeniedException.getMessage())
                .path(request.getRequestURI())
                .build();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        PrintWriter writer = response.getWriter();
        writer.println(mapper.writeValueAsString(restApiResponseErrorMessage));
    }
}
