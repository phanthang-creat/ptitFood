package com.server.ptitFood.controllers.admin;
import com.server.ptitFood.domain.dto.LoginDto;
import com.server.ptitFood.domain.entities.Admin;
import com.server.ptitFood.domain.exceptions.UsernameOrPasswordNotValid;
import com.server.ptitFood.domain.services.admin.AdminControlService;
import com.server.ptitFood.security.Jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;


@Controller()
@RequestMapping(path = "/auth/admin")
public class AdminAuthentication {

    private final AdminControlService adminControlService;
    private final JwtTokenProvider jwtTokenProvider;

    private final MessageSource messageSource;

    public AdminAuthentication(AdminControlService adminControlService, AdminControlService adminControlService1, JwtTokenProvider jwtTokenProvider, MessageSource messageSource) {
        this.adminControlService = adminControlService1;
        this.jwtTokenProvider = jwtTokenProvider;
        this.messageSource = messageSource;
    }

    @GetMapping("login")
    public String login(Model model, HttpServletRequest request) {

        model.addAttribute("account",new LoginDto());
        String message = messageSource.getMessage("hello", null, "default message", request.getLocale());
        model.addAttribute("message", message);

        return "web/admin/login";
    }

    @PostMapping(
            value = "login",
            produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    MediaType.APPLICATION_ATOM_XML_VALUE
            }
    )
    public String login(
            @Valid LoginDto loginDto,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws UsernameOrPasswordNotValid {
        try {
            if (request.getCookies() != null) {
                // TODO: Delete all cookies
                for (Cookie cookie : request.getCookies()) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }

            Admin admin = adminControlService.login(loginDto);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    admin.getUsername(),
                    admin.getPassword(),
                    admin.getAuthorities()
            );

            String token = jwtTokenProvider.generateToken(authentication);

            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(cookie);


            return "redirect:/admin/home";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("param", Map.of(
                    "error", e.getMessage()
            ));
            model.addAttribute("account", new LoginDto(
                    loginDto.getUsername()
            ));
            return "web/admin/login";
        }
    }

}
