package com.server.ptitFood.controllers;

import com.server.ptitFood.config.reCaptcha.CaptchaService;
import com.server.ptitFood.config.reCaptcha.ReCaptchaInvalidException;
import com.server.ptitFood.domain.dto.LoginDto;
import com.server.ptitFood.domain.dto.RegisterDto;
import com.server.ptitFood.domain.dto.VerifyEmailDto;
import com.server.ptitFood.domain.entities.Customer;
import com.server.ptitFood.domain.exceptions.UserAlreadyExistException;
import com.server.ptitFood.domain.services.UserService;
import com.server.ptitFood.security.Jwt.JwtTokenProvider;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@Controller
@RequestMapping("auth")
public class Authentication {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    private final CaptchaService captchaService;

    private final Bucket bucket;


    public Authentication(UserService userService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, CaptchaService captchaService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.captchaService = captchaService;
        Bandwidth limit = Bandwidth
                .classic(5, Refill.intervally(1, java.time.Duration.ofMinutes(1)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();

    }

    @GetMapping("login")
    public String login(
            Model model,
            HttpServletRequest request
    ) {
        model.addAttribute("account", new LoginDto());
        model.addAttribute("captchaFailed", null);
        model.addAttribute("loginFailed", null);

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("token")) {
                    return "redirect:/";
                }
            }
        }

        return "web/portal/auth/index";
    }

    @GetMapping("register")
    public String register(
            Model model,
            HttpServletRequest request
    ) {
        model.addAttribute("account", new RegisterDto());
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("token")) {
                    return "redirect:/";
                }
            }
        }

        return "web/portal/auth/register";
    }

    @PostMapping(
            value = "register"
    )
    @Transactional
    public String register(
            RegisterDto registerDto,
            HttpServletRequest request,
            Model model
        )
            throws UserAlreadyExistException
    {
        try {
            String captcha = request.getParameter("g-recaptcha-response");

            captchaService.processResponse(captcha);

            boolean result = userService.register(registerDto);

            if (result) {
                model.addAttribute("account", new RegisterDto());
                model.addAttribute("message", "Register success");
                model.addAttribute("verifyEmail", registerDto.getEmail());
                return "web/portal/auth/verify-email";
            } else {
                model.addAttribute("account", registerDto);
                model.addAttribute("message", "Register failed");
            }

            return "web/portal/auth/register";
        } catch (Exception e) {
            if (e instanceof ReCaptchaInvalidException) {
                model.addAttribute("captchaFailed", "Captcha invalid");
            }
            model.addAttribute("account", registerDto);
            model.addAttribute("message", e.getMessage());
            return "web/portal/auth/register";
        }
    }

    @PostMapping("login")
    @Transactional
    public String login(
            LoginDto loginDto,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) throws Exception {
        try {
            if (bucket.tryConsume(1)) {

            } else {
                model.addAttribute("account", new LoginDto());
                model.addAttribute("captchaFailed", null);
                model.addAttribute("loginFailed", "Thao tác quá nhanh, vui lòng thử lại sau 1 phút");
                return "web/portal/auth/index";
            }
            String captcha = request.getParameter("g-recaptcha-response");

            captchaService.processResponse(captcha);

            Customer user = userService.login(loginDto);

            org.springframework.security.core.Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword(),
                    user.getAuthorities()
            );
            String token = jwtTokenProvider.generateToken(authentication);

            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 30);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            response.addCookie(cookie);

            return "redirect:/";

        } catch (Exception e) {
            if (e instanceof ReCaptchaInvalidException) {
               model.addAttribute("captchaFailed", "Captcha invalid");
            }

            model.addAttribute("loginFailed", "Login failed");

            model.addAttribute("account", new LoginDto(loginDto.getUsername()));

            return "web/portal/auth/index";
        }
    }

//    @PostMapping("refresh/otp")


    @GetMapping("verify")
    public String verify(
            Model model,
            HttpServletRequest request
    ) {
        return "web/portal/auth/verify-email";
    }

    @PostMapping("verify")
    @Transactional
    public String verify(
            @Valid VerifyEmailDto verifyEmailDto,
            Model model
    ) throws UserAlreadyExistException {
        try {
            userService.verify(verifyEmailDto);
            return "redirect:/auth/login";
        } catch (UserAlreadyExistException e) {
            model.addAttribute("verifyEmail", verifyEmailDto.getEmail());
            model.addAttribute("message", e.getMessage());
            return "web/portal/auth/verify-email";
        }
    }

    @PostMapping("resend")
    @Transactional
    public String reSendOTP(
            @RequestParam String email,
            Model model
    ) throws UserAlreadyExistException {
        userService.reSendOtp(email);
        model.addAttribute("verifyEmail", email);
        model.addAttribute("message", "Mã OTP đã được gửi lại");
        return "web/portal/auth/verify-email";
    }

    @PostMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/auth/login";
    }
}