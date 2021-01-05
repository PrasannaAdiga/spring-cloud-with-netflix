package com.learning.cloud.clinet.config;

import com.learning.cloud.clinet.exception.handler.FeignErrorDecoder;
import feign.Logger;;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }
}
