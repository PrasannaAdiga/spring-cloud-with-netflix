package com.learning.cloud.config;

import com.learning.cloud.filter.APIAccessLogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class APIAccessLogFilterConfig {
    @Bean
    public FilterRegistrationBean<APIAccessLogFilter> serverAccessLogFilterRegistration() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean<APIAccessLogFilter>();
        registrationBean.setFilter(new APIAccessLogFilter());
        registrationBean.addUrlPatterns("/v1/accounts/*");
        return registrationBean;
    }
}
