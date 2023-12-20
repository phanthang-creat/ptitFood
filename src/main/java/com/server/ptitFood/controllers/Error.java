package com.server.ptitFood.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class Error implements ErrorController {
    private static final String PATH = "/error";

    @RequestMapping(PATH)
    public String handleError(final HttpServletRequest request,
                                                    final HttpServletResponse response) throws Throwable {
        if (response.getStatus() == HttpServletResponse.SC_UNAUTHORIZED) {
            if (request.getRequestURI().contains("/auth/admin")) {
                response.sendRedirect("/auth/admin/login");
            } else {
                response.sendRedirect("/auth/login");
            }
        }
        return "/web/404";
    }

    public String getErrorPath() {
        return PATH;
    }

}
