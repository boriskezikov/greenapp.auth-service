package com.greenapp.authservice.security;

import com.netflix.hystrix.exception.HystrixBadRequestException;

public class NotAuthorizedException extends RuntimeException {

    public NotAuthorizedException(String message) {
        super(message);
    }
}
