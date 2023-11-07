package com.server.ptitFood.domain.User.controllers;

import com.server.ptitFood.domain.User.exceptions.UserAlreadyExistException;
import com.server.ptitFood.domain.User.exceptions.UsernameOrPasswordNotValid;
import com.server.ptitFood.domain.User.models.dto.LoginDto;
import com.server.ptitFood.domain.User.models.dto.RegisterDto;
import com.server.ptitFood.domain.User.models.dto.VerifyEmailDto;
import com.server.ptitFood.domain.User.services.UserService;
import com.server.ptitFood.security.Jwt.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("auth")
public class Authentication {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public Authentication(UserService userService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("register")
    public ResponseEntity<Map<Object, Object>> register(@RequestBody @Valid RegisterDto registerDto) throws UserAlreadyExistException {
        userService.register(registerDto);
        Map<Object, Object> model = new HashMap<>();
        model.put("message", "OTP sent to your email");
        return ok(model);
    }

    @PostMapping("login")
    public ResponseEntity<Map<Object, Object>> login(
            @RequestBody @Valid LoginDto loginDto
    ) throws Exception {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );
            String token = jwtTokenProvider.generateToken(authentication);
            System.out.println(token);
            Map<Object, Object> model = new HashMap<>();
            model.put("username", loginDto.getUsername());
            model.put("token", token);
            return ok(model);
        } catch (Exception e) {
            throw new UsernameOrPasswordNotValid("Username or password not valid");
        }
    }

    @PostMapping("verify")
    public ResponseEntity<Map<Object, Object>> verify(@RequestBody @Valid VerifyEmailDto verifyEmailDto) throws UserAlreadyExistException {
        userService.verify(verifyEmailDto);
        Map<Object, Object> model = new HashMap<>();
        model.put("message", "Register success");
        return ok(model);
    }
}