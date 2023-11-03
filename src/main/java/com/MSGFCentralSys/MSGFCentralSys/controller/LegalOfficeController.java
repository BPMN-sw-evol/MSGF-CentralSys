package com.MSGFCentralSys.MSGFCentralSys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class LegalOfficeController {

    @GetMapping({"/LegalOffice"})
    public String LegalOfficeView(Model model) {
        model.addAttribute("titulo","");
        return "views/LegalOffice";
    }
}
