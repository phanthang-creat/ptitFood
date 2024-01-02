package com.server.ptitFood.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class Error implements ErrorController {
    private static final String PATH = "/error";

    @RequestMapping(PATH)
    public String handleError(final HttpServletRequest request,
                                                    final HttpServletResponse response) throws Throwable {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "/web/404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "/web/404";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "redirect:/auth/logout";
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return "redirect:/auth/logout";
            }
        }

        System.out.println("Error: " + status);

        return "/web/404";
    }

    public String getErrorPath() {
        return PATH;
    }

}
