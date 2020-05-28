package com.greenapp.authservice.security;


import com.netflix.hystrix.exception.HystrixBadRequestException;

public class NotAuthorizedException extends HystrixBadRequestException {

    public NotAuthorizedException(String message) {
        super(message);
    }
}
