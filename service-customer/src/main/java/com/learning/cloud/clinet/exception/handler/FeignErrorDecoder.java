package com.learning.cloud.clinet.exception.handler;

import com.learning.cloud.exception.custom.BadRequestException;
import com.learning.cloud.exception.custom.ResourceNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400:
                return new BadRequestException("Bad Request!");
            case 404:
                return new ResourceNotFoundException("Requested resource not found!");
            default:
                return new Exception(response.reason());
        }
    }
}
