package com.learning.cloud.security.config;

import com.learning.cloud.security.RestAccessDeniedErrorHandler;
import com.learning.cloud.security.RestUnAuthorizedErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class CustomWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final RestUnAuthorizedErrorHandler restUnAuthorizedErrorHandler;
    private final RestAccessDeniedErrorHandler restAccessDeniedErrorHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/actuator").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(restAccessDeniedErrorHandler)
                .authenticationEntryPoint(restUnAuthorizedErrorHandler) // To handle 401 Unauthorized exception
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password(passwordEncoder().encode("password")).authorities("ROLE_USER");
        auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder().encode("password")).authorities("ROLE_ADMIN");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
