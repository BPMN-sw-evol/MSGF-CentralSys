package com.MSGFCentralSys.MSGFCentralSys.controller;

import com.MSGFCentralSys.MSGFCentralSys.dto.CreditRequestDTO;
import com.MSGFCentralSys.MSGFCentralSys.dto.TaskInfo;
import com.MSGFCentralSys.MSGFCentralSys.services.CreditAnalystCoupleServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String CreditAnalystCoupleView(Model model){

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
        model.addAttribute("titulo", "Analyze Couple Applications Information ");
        return "views/CreditAnalystCouple";
    }

    @PostMapping("/view-creditcredit-analyst-couple")
    public  String viewTaskCouple(@RequestParam(name = "processId") String processId, Model model){
        CreditRequestDTO creditRequestDTO = this.creditAnalystCoupleServices.getProcessVariablesById(processId);
        TaskInfo taskInfo = this.creditAnalystCoupleServices.getTaskInfoByProcessId(processId);
        creditRequestDTO.setTaskInfo(taskInfo);
        model.addAttribute("creditRequestDTO", creditRequestDTO);
            model.addAttribute("titulo", "Couple Application Information");
        return  "modals/Couple";
    }

    @PostMapping("/approve-credit-analyst-couple")
    public String approveTaskCouple(@RequestParam(name = "processId") String processId){
        this.creditAnalystCoupleServices.approveTask(processId);
        return "redirect:/credit-analyst-couple";
    }

    @PostMapping("/reject-credit-analyst-couple")
    public String rejectTaskCouple(@RequestParam(name = "processId") String processId){
        this.creditAnalystCoupleServices.rejectTask(processId);
        return "redirect:/credit-analyst-couple";
    }

}
