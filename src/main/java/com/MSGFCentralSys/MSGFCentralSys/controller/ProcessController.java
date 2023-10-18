package com.MSGFCentralSys.MSGFCentralSys.controller;

import com.MSGFCentralSys.MSGFCentralSys.services.CamundaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProcessController {

    private final CamundaService camundaService;

    public ProcessController(CamundaService camundaService) {
        this.camundaService = camundaService;
    }

    @GetMapping("/completeCreditAnalyst")
    public String completeTask(@RequestParam(name = "taskId") String taskId, @RequestParam(name="assignee") String assignee, @RequestParam(name="value") Boolean value, @RequestParam("variable") String variable) {
        String resultado = this.camundaService.completeTask(taskId, assignee, value, variable);
        return "redirect:/CreditAnalyst";
    }
}
