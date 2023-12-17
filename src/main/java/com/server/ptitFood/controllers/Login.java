package com.server.ptitFood.controllers;

import com.server.ptitFood.domain.dto.LoginDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/")
public class Login {
    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("account", new LoginDto());
        return "web/portal/login/index";
    }
}
