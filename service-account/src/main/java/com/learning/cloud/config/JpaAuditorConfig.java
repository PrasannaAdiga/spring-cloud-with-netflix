package com.learning.cloud.config;

import com.learning.cloud.config.impl.JpaAuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaAuditorConfig {
    @Bean
    public AuditorAware<String> auditorAware(){
        return new JpaAuditorAwareImpl();
    }

}
