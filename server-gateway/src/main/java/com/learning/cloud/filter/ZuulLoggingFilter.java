package com.learning.cloud.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class ZuulLoggingFilter extends ZuulFilter {
    //Filter type can be either 'pre', 'post' or  'error'
    // pre - before request has executed, post - after request has processed
    // error - if request has caused any error
    @Override
    public String filterType() {
        return "pre";
    }

    //Order of this filter
    @Override
    public int filterOrder() {
        return 0;
    }

    //Execute this filter or not based on any expression
    @Override
    public boolean shouldFilter() {
        return true;
    }


    @Override
    public Object run() throws ZuulException {
        HttpServletRequest httpServletRequest = RequestContext.getCurrentContext().getRequest();
        log.info("request: {}, request URI: {}", httpServletRequest, httpServletRequest.getRequestURI());
        return null;
    }
}
