package com.learning.cloud.util;

import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Enumeration;

public class HttpLogUtil {
    private HttpLogUtil() {}

    public static String getRequestBody(ContentCachingRequestWrapper requestWrapper) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(requestWrapper, ContentCachingRequestWrapper.class);
        if(wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if(buf.length > 0) {
                String payload = getPayload(wrapper.getCharacterEncoding(), buf);
                return payload.replace("\\n", "");
            }
        }
        return "";
    }

    public static String getResponseBody(ContentCachingResponseWrapper responseWrapper) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(responseWrapper, ContentCachingResponseWrapper.class);
        if(wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if(buf.length > 0) {
                String payload = getPayload(wrapper.getCharacterEncoding(), buf);
                return payload.replace("\\n", "");
            }
        }
        return "";
    }

    private static String getPayload(String characterEncoding, byte[] buf) {
        String payload;
        try {
            payload = new String(buf, 0, buf.length, characterEncoding);
        } catch (UnsupportedEncodingException ex) {
            payload = "[UNKNOWN]";
        }
        return payload;
    }

    public static HttpHeaders getHttpHeaders(HttpServletRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames != null && headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> headerValues = request.getHeaders(headerName);
            while(headerValues.hasMoreElements()) {
                httpHeaders.add(headerName, headerValues.nextElement());
            }
        }
        return httpHeaders;
    }

    public static HttpHeaders getHttpHeaders(HttpServletResponse response) {
        HttpHeaders httpHeaders = new HttpHeaders();
        Collection<String> headerNames = response.getHeaderNames();
        if(!CollectionUtils.isEmpty(headerNames)) {
            for(String headerName : headerNames) {
                Collection<String> headerValues = response.getHeaders(headerName);
                for(String headerValue : headerValues) {
                    httpHeaders.add(headerName, headerValue);
                }
            }
        }
        return httpHeaders;
    }
}
