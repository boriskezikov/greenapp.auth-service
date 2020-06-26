package com.greenapp.shopcatalogmanager.security;


import com.greenapp.authservice.security.NotAuthorizedException;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

@Component
public class SecurityFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(SecurityFilter.class.getName());

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        var excludeAuth = new ArrayList<>(Arrays.asList("api-docs", "configuration", "swagger", "webjars", "error"));
        var authHeader = req.getHeader("X-GREEN-APP-ID");
        AtomicBoolean exclude = new AtomicBoolean(false);

        LOG.info("-------------------------------------------------------------------------------------------");
        LOG.info(" /" + req.getMethod());
        LOG.info(" Request: " + req.getRequestURI());
        LOG.info("-------------------------------------------------------------------------------------------");

        var uri = req.getRequestURI();

        excludeAuth.forEach(ex -> {
            if (uri.contains(ex)) {
                exclude.set(true);
            }
        });

        if (!exclude.get() && (authHeader == null || !Objects.equals(authHeader, "GREEN"))) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setContentType("application/json");
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Required headers not specified in the request or incorrect");
            throw new NotAuthorizedException("Required headers not specified in the request or incorrect");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        LOG.warning("Auth filter initialization");
    }
}