package com.MSGFCentralSys.MSGFCentralSys.controller;

import com.MSGFCentralSys.MSGFCentralSys.dto.CreditRequestDTO;
import com.MSGFCentralSys.MSGFCentralSys.dto.TaskInfo;
import com.MSGFCentralSys.MSGFCentralSys.services.CreditAnalystCoupleServices;
import com.MSGFCentralSys.MSGFCentralSys.services.CreditAnalystValidateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CreditAnalystController {

    private final CreditAnalystCoupleServices creditAnalystCoupleServices;
    private final CreditAnalystValidateService creditAnalystValidateService;
    List<CreditRequestDTO> processVariablesListCA = new ArrayList<>();

    public CreditAnalystController(CreditAnalystCoupleServices creditAnalystCoupleServices, CreditAnalystValidateService creditAnalystValidateService) {
        this.creditAnalystCoupleServices = creditAnalystCoupleServices;
        this.creditAnalystValidateService = creditAnalystValidateService;
    }


    @GetMapping("/credit-analyst-couple")
    public String CreditAnalystCoupleView(Model model) throws IOException {

        List<String> processIds = this.creditAnalystCoupleServices.getAllProcessByActivityId("Activity_0h13zv2");
        System.out.println(processIds.toString());
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

    @GetMapping("/complete-credit-analyst-couple")
    public String completeTaskCreditAnalystCouple(@RequestParam(name = "taskId") String taskId, @RequestParam(name="assignee") String assignee, @RequestParam(name="value") Boolean value, @RequestParam("variable") String variable) throws IOException {
        System.out.println("aqui estoy"+value);
        System.out.println("aqui estoy"+assignee);

        this.creditAnalystCoupleServices.completeTask(taskId, assignee, value, variable);
        return "redirect:/credit-analyst-couple";
    }

    @GetMapping("/complete-credit-analyst-validate")
    public String completeTaskCreditAnalystValidate(@RequestParam(name = "taskId") String taskId, @RequestParam(name="assignee") String assignee, @RequestParam(name="value") Boolean value, @RequestParam("variable") String variable) throws IOException {
        System.out.println("aqui estoy"+value);
        System.out.println("aqui estoy"+assignee);

        this.creditAnalystValidateService.completeTask(taskId, assignee, value, variable);
        return "redirect:/credit-analyst-couple";
    }
}
