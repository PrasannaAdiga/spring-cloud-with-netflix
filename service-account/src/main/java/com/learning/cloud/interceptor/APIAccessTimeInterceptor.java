package com.learning.cloud.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class APIAccessTimeInterceptor implements HandlerInterceptor {
    private static final String TIMER = "Timer";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(TIMER, System.currentTimeMillis());
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        Long processTimeInMS = System.currentTimeMillis() - (Long) request.getAttribute(TIMER);
        log.debug("Total time taken to process the REST API {}:{} is {}ms", request.getMethod(), request.getRequestURI(), processTimeInMS);
        request.removeAttribute(TIMER);
    }
}
