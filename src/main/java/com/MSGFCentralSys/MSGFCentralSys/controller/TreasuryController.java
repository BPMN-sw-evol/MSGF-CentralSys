package com.MSGFCentralSys.MSGFCentralSys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TreasuryController {

    @GetMapping({"/Treasury"})
    public String TreasuryView(Model model) {
        model.addAttribute("titulo","Welcome to the MsgFoundation's CREDIT REQUEST");
        return "views/Treasury";
    }
}
