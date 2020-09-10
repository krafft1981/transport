package com.rental.transport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value="/")
@Controller
public class IndexController {

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
