package com.learning.cloud.config;

import feign.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FeignConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
        if(log.isDebugEnabled()) {
            return Logger.Level.FULL;
        } else {
            return Logger.Level.BASIC;
        }
    }
}
