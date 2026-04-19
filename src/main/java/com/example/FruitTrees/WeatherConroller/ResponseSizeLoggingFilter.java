package com.example.FruitTrees.WeatherConroller;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
public class ResponseSizeLoggingFilter extends OncePerRequestFilter {

    private static final Logger log =  LoggerFactory.getLogger(ResponseSizeLoggingFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        ContentCachingResponseWrapper wrappedResponse =
                new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(request, wrappedResponse);
        } finally {
            byte[] content = wrappedResponse.getContentAsByteArray();
            log.info("Response bytes for {} {}: {}", request.getMethod(), request.getRequestURI(), content.length);
            wrappedResponse.copyBodyToResponse();
        }
    }
}