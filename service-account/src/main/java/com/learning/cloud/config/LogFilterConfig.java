package com.learning.cloud.config;

import com.learning.cloud.filter.ServerAccessLogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogFilterConfig {
    @Bean
    public FilterRegistrationBean<ServerAccessLogFilter> serverAccessLogFilterRegistration() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean<ServerAccessLogFilter>();
        registrationBean.setFilter(new ServerAccessLogFilter());
        registrationBean.addUrlPatterns("/v1/accounts/*");
        return registrationBean;
    }
}
