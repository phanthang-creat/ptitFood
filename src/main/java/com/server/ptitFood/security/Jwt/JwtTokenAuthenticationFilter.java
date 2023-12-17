package com.server.ptitFood.security.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter extends GenericFilterBean {
    public static final String HEADER_PREFIX = "";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String token = resolveToken((HttpServletRequest) request);

        System.out.println("doFilter on request: " + ((HttpServletRequest) request).getServletPath());

        if (token != null) {
            if (jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(auth);
                    SecurityContextHolder.setContext(context);
                }
            } else {
                // Clear security context
                SecurityContextHolder.clearContext();

                // Clear token
                Cookie cookie = new Cookie("token", null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                ((HttpServletResponse) response).addCookie(cookie);
                ((HttpServletResponse) response).sendRedirect("/auth/admin/login");
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
//        String bearerToken = request.getCookies() != null ? request.getCookies()[0].getValue() : null;
//        if (StringUtils.hasText(bearerToken)) {
//            return bearerToken;
//        }

        String token = null;

        Cookie[] cookies = request.getCookies() != null ? request.getCookies() : null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                }
            }
        }

        if (StringUtils.hasText(token)) {
            return token;
        }

        return null;
    }
}

