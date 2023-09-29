package com.msgfoundation.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;


@Controller
public class HomeController {
    @GetMapping("")
    public String mainView(Model model){
        return "inicio";
    }
}
