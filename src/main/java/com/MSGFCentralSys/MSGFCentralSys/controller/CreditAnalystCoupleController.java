package com.MSGFCentralSys.MSGFCentralSys.controller;

import com.MSGFCentralSys.MSGFCentralSys.dto.CreditRequestDTO;
import com.MSGFCentralSys.MSGFCentralSys.dto.TaskInfo;
import com.MSGFCentralSys.MSGFCentralSys.services.CreditAnalystCoupleServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CreditAnalystCoupleController {

    private final CreditAnalystCoupleServices creditAnalystCoupleServices;
    List<CreditRequestDTO> processVariablesListCA = new ArrayList<>();

    public CreditAnalystCoupleController(CreditAnalystCoupleServices creditAnalystCoupleServices) {
        this.creditAnalystCoupleServices = creditAnalystCoupleServices;
    }


    @GetMapping("/credit-analyst-couple")
    public String CreditAnalystCoupleView(Model model) throws IOException {

        List<String> processIds = this.creditAnalystCoupleServices.getAllProcessByActivityId("Activity_0h13zv2");
        processVariablesListCA.clear();
        // Iterar a través de los processIds y obtener las variables para cada uno
        for (String processId : processIds) {
            CreditRequestDTO creditRequestDTO = this.creditAnalystCoupleServices.getProcessVariablesById(processId);
            TaskInfo taskInfo = this.creditAnalystCoupleServices.getTaskInfoByProcessId(processId);
            creditRequestDTO.setTaskInfo(taskInfo);
            processVariablesListCA.add(creditRequestDTO);
        }

        // Agregar la lista de variables de proceso al modelo para pasarla a la vista
        model.addAttribute("processVariablesList", processVariablesListCA);
        model.addAttribute("titulo", "Analyze Information Couple of Applications");
        return "views/CreditAnalystCouple";

    }

    @PostMapping("/approve-credit-analyst-couple")
    public String approveTaskCouple(@RequestParam(name = "taskId") String taskId){
        this.creditAnalystCoupleServices.approveTask(taskId);
        return "redirect:/credit-analyst-couple";
    }

    @PostMapping("/rejected-credit-analyst-couple")
    public String rejectedTaskCouple(@RequestParam(name = "taskId") String taskId){
        this.creditAnalystCoupleServices.rejectedTask(taskId);
        return "redirect:/credit-analyst-couple";
    }

}
