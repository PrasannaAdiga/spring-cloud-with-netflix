package com.learning.cloud.filter;

import com.learning.cloud.util.HttpLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class APIAccessLogFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(log.isDebugEnabled()) {
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
            filterChain.doFilter(requestWrapper, responseWrapper);
            logRequest(requestWrapper);
            logResponse(requestWrapper, responseWrapper);
            responseWrapper.copyBodyToResponse();
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void logResponse(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper) {
        try {
            String responseBodyStr = HttpLogUtil.getResponseBody(responseWrapper);
            HttpStatus httpStatus = HttpStatus.valueOf(responseWrapper.getStatus());
            HttpHeaders requestHttpHeaders = HttpLogUtil.getHttpHeaders(requestWrapper);
            HttpHeaders responseHttpHeaders = HttpLogUtil.getHttpHeaders(responseWrapper);
            if(log.isDebugEnabled()) {
                log.debug("\n=====Response=====\n>> {} {} \n>> Request Headers: {}\n>> Headers: {}\n>> Body: {}\n==================",
                        httpStatus, httpStatus.getReasonPhrase(), requestHttpHeaders, responseHttpHeaders, responseBodyStr);
            }
        } catch(Exception ex) {
            log.debug(ex.getMessage());
        }
    }

    private void logRequest(ContentCachingRequestWrapper requestWrapper) {
        try {
            String requestBodyStr = HttpLogUtil.getRequestBody(requestWrapper);
            HttpHeaders requestHttpHeaders = HttpLogUtil.getHttpHeaders(requestWrapper);
            String queryParameter = "";
            if(StringUtils.hasText(requestWrapper.getQueryString())) {
                queryParameter = "?" + requestWrapper.getQueryString();
            }
            if(log.isDebugEnabled()) {
                log.debug("\n=====Request======\n>> {} {}{}\n>> Headers: {}\n>> Body: {}\n==================",
                        requestWrapper.getMethod(), requestWrapper.getRequestURI(), queryParameter, requestHttpHeaders,
                        requestBodyStr.replace("\r", "").replace("\n", "").replace(" ", ""));
            }
        } catch(Exception ex) {
            log.debug(ex.getMessage());
        }
    }
}
