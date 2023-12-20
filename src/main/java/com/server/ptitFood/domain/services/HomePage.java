package com.server.ptitFood.domain.services;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class HomePage {
    @RequestMapping("")
    public String homePage() {
        return "web/admin/home";
    }
}
