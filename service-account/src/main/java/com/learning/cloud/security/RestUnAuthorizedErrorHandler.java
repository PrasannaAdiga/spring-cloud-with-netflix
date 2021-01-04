package com.learning.cloud.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.learning.cloud.exception.response.RestApiResponseErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@Component
public class RestUnAuthorizedErrorHandler extends BasicAuthenticationEntryPoint {
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        RestApiResponseErrorMessage restApiResponseErrorMessage = RestApiResponseErrorMessage.builder().timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("User is not Authenticated")
                .message(authException.getMessage())
                .path(request.getRequestURI())
                .build();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter writer = response.getWriter();
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
        writer.println(mapper.writeValueAsString(restApiResponseErrorMessage));
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("account-service");
        super.afterPropertiesSet();
    }
}
