package com.server.ptitFood.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomePageCtl {
    @RequestMapping("/")
    public String index() {
        return "web/portal/home/index";
    }
}
