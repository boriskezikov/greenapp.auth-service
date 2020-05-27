package com.greenapp.authservice.security;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
public class RequestFilter implements HandlerInterceptor {

    @SneakyThrows
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {

        String headerValue = request.getHeader("X-GREEN-APP-ID");
        if (headerValue == null || !Objects.equals(headerValue,"GREEN")) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}
