package com.learning.cloud.config;

import com.learning.cloud.interceptor.APIAccessTimeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class APIAccessTimerInterceptorConfig implements WebMvcConfigurer {
    private final APIAccessTimeInterceptor apiAccessTimeInterceptor;

    private static final String URL = "/v1/accounts/*";

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiAccessTimeInterceptor).addPathPatterns(URL);
    }
}
