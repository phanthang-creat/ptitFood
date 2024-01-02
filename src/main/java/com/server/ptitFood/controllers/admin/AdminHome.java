package com.server.ptitFood.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "admin")
public class AdminHome {
    @RequestMapping("/home")
    public String home() {
        return "web/admin/home/dashboard";
    }
}
