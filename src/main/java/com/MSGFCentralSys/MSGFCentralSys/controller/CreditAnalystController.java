package com.MSGFCentralSys.MSGFCentralSys.controller;

import com.MSGFCentralSys.MSGFCentralSys.dto.CreditRequestDTO;
import com.MSGFCentralSys.MSGFCentralSys.dto.TaskInfo;
import com.MSGFCentralSys.MSGFCentralSys.services.CreditAnalystServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CreditAnalystController {

    private final CreditAnalystServices camundaService;
    List<CreditRequestDTO> processVariablesListCA = new ArrayList<>();

    public CreditAnalystController(CreditAnalystServices camundaService) {
        this.camundaService = camundaService;
    }


    @GetMapping("/CreditAnalyst")
    public String CreditAnalystView(Model model) throws IOException {

        List<String> processIds = this.camundaService.getAllProcessByCreditAnalyst("CreditAnalyst");
        processVariablesListCA.clear();
        // Iterar a través de los processIds y obtener las variables para cada uno
        for (String processId : processIds) {
            CreditRequestDTO creditRequestDTO = this.camundaService.getProcessVariablesById(processId);
            TaskInfo taskInfo = this.camundaService.getTaskInfoByProcessId(processId);
            creditRequestDTO.setTaskInfo(taskInfo);
            processVariablesListCA.add(creditRequestDTO);
        }

        // Agregar la lista de variables de proceso al modelo para pasarla a la vista
        model.addAttribute("processVariablesList", processVariablesListCA);
        model.addAttribute("titulo", "Analyze applications");
        return "views/CreditAnalyst";

    }

    @GetMapping("/completeCreditAnalyst")
    public String completeTaskCreditAnalyst(@RequestParam(name = "taskId") String taskId, @RequestParam(name="assignee") String assignee, @RequestParam(name="value") Boolean value, @RequestParam("variable") String variable) throws IOException {
        System.out.println("aqui estoy"+value);
        System.out.println("aqui estoy"+assignee);

        this.camundaService.completeTask(taskId, assignee, value, variable);
        return "redirect:/CreditAnalyst";
    }
}
