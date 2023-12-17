package com.server.ptitFood.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilterConfig extends OncePerRequestFilter {

    public JwtFilterConfig() {

    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final Cookie[] cookies = request.getCookies() != null ? request.getCookies() : null;

            String authorizationHeader = null;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("token")) {
                        System.out.println(cookie.getValue());
                        authorizationHeader = cookie.getValue();
                    }
                }
            }

            // Check if Authorization header is null or not

            // Check if Authorization header is valid or not
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("JWT Token is missing");
        }
        // Get Authorization header
    }

}
