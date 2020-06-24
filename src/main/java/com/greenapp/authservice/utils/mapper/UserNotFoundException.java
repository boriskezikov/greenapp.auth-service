package com.greenapp.authservice.utils.mapper;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Requested user is not registered!")
public class UserNotFoundException extends RuntimeException {
}
