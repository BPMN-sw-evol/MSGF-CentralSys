package com.MSGFCentralSys.MSGFCentralSys.controller;

import com.MSGFCentralSys.MSGFCentralSys.dto.CreditRequestDTO;
import com.MSGFCentralSys.MSGFCentralSys.dto.TaskInfo;
import com.MSGFCentralSys.MSGFCentralSys.services.CreditCommitteServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CreditCommitteController {
    private final CreditCommitteServices camundaService;
    List<CreditRequestDTO> processVariablesListCC = new ArrayList<>();

    public CreditCommitteController(CreditCommitteServices camundaService) {
        this.camundaService = camundaService;
    }

    @GetMapping({"/credit-committee"})
    public String CreditCommitteView(Model model) throws IOException {
        List<String> processIds = this.camundaService.getAllProcessByCreditCommitte("CreditCommitte");
        processVariablesListCC.clear();

        // Crear una lista para almacenar información de variables de proceso
        // Iterar a través de los processIds y obtener las variables para cada uno
        for (String processId : processIds) {
            CreditRequestDTO creditRequestDTO = this.camundaService.getProcessVariablesById(processId);
            TaskInfo taskInfo = this.camundaService.getTaskInfoByProcessId(processId);
            creditRequestDTO.setTaskInfo(taskInfo);
            System.out.println("task info: "+taskInfo.toString());
            processVariablesListCC.add(creditRequestDTO);
        }


        // Agregar la lista de variables de proceso al modelo para pasarla a la vista
        model.addAttribute("processIds", processIds);
        model.addAttribute("processVariablesList", processVariablesListCC);
        model.addAttribute("titulo","Assess Applications");
        return "views/CreditCommittee";
    }

    @GetMapping("/complete-credit-committee")
    public String completeTaskCreditCommitte(@RequestParam(name = "taskId") String taskId, @RequestParam(name="assignee") String assignee, @RequestParam(name="value") String value, @RequestParam("variable") String variable) throws IOException {
        this.camundaService.completeTask(taskId, assignee, value, variable);
        return "redirect:/credit-committee";
    }
}
