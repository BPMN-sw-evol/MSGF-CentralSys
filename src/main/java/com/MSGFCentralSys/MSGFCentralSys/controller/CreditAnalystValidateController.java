package com.MSGFCentralSys.MSGFCentralSys.controller;

import com.MSGFCentralSys.MSGFCentralSys.dto.CreditRequestDTO;
import com.MSGFCentralSys.MSGFCentralSys.dto.TaskInfo;
import com.MSGFCentralSys.MSGFCentralSys.services.CreditAnalystCoupleServices;
import com.MSGFCentralSys.MSGFCentralSys.services.CreditAnalystValidateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CreditAnalystValidateController {

    private final CreditAnalystValidateService creditAnalystValidateService;
    List<CreditRequestDTO> processVariablesListCA = new ArrayList<>();

    public CreditAnalystValidateController(CreditAnalystValidateService creditAnalystValidateService) {
        this.creditAnalystValidateService = creditAnalystValidateService;
    }

    @GetMapping("/credit-analyst-validate")
    public String CreditAnalystValidateView(Model model) throws IOException {

        List<String> processIds = this.creditAnalystValidateService.getAllProcessByActivityId("Activity_0w7pg72");
        System.out.println(processIds.toString());
        processVariablesListCA.clear();
        // Iterar a través de los processIds y obtener las variables para cada uno
        for (String processId : processIds) {
            CreditRequestDTO creditRequestDTO = this.creditAnalystValidateService.getProcessVariablesById(processId);
            TaskInfo taskInfo = this.creditAnalystValidateService.getTaskInfoByProcessId(processId);
            creditRequestDTO.setTaskInfo(taskInfo);
            processVariablesListCA.add(creditRequestDTO);
        }

        // Agregar la lista de variables de proceso al modelo para pasarla a la vista
        model.addAttribute("processVariablesList", processVariablesListCA);
        model.addAttribute("titulo", "Verify Validity of Applications");
        return "views/CreditAnalystValidate";

    }

    @PostMapping("/approve-credit-analyst-validate")
    public String approveTaskValidate(@RequestParam(name = "taskId") String taskId){
        this.creditAnalystValidateService.approveTask(taskId);
        return "redirect:/credit-analyst-validate";
    }

    @PostMapping("/rejected-credit-analyst-validate")
    public String rejectedTaskValidate(@RequestParam(name = "taskId") String taskId){
        this.creditAnalystValidateService.rejectedTask(taskId);
        return "redirect:/credit-analyst-validate";
    }
}
