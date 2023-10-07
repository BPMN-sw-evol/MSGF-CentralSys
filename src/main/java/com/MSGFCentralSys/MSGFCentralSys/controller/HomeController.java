package com.MSGFCentralSys.MSGFCentralSys.controller;

import com.MSGFCentralSys.MSGFCentralSys.services.AnalystService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final AnalystService analystService;

    @Autowired
    public HomeController(AnalystService analystService) {
        this.analystService = analystService;
    }

    @GetMapping({"/"})
    public String mainView(Model model) {
        model.addAttribute("titulo","Welcome to the MsgFoundation's CREDIT REQUEST");
        return "init";
    }

    @GetMapping({"/CreditAnalyst"})
    public String CreditAnalystView(Model model) {

        String applicantData = this.analystService.getTasksForProcessDefinition();
        List<String> application = new ArrayList<>();

        model.addAttribute("application",application);
        model.addAttribute("titulo","Review Applications");
        return "views/CreditAnalyst";
    }

    @GetMapping({"/CreditCommitte"})
    public String CreditCommitteView(Model model) {
        model.addAttribute("titulo","Assess Applications");
        return "views/CreditCommitte";
    }
    @GetMapping({"/LegalOffice"})
    public String LegalOfficeView(Model model) {
        model.addAttribute("titulo","");
        return "views/LegalOffice";
    }
    @GetMapping({"/Treasury"})
    public String TreasuryView(Model model) {
        model.addAttribute("titulo","Welcome to the MsgFoundation's CREDIT REQUEST");
        return "views/Treasury";
    }
}
