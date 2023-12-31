package com.MSGFCentralSys.MSGFCentralSys.controller;

import com.MSGFCentralSys.MSGFCentralSys.dto.CreditRequestDTO;
import com.MSGFCentralSys.MSGFCentralSys.dto.TaskInfo;
import com.MSGFCentralSys.MSGFCentralSys.services.CreditCommitteServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CreditCommitteController {
    private final CreditCommitteServices creditCommitteServices;
    List<CreditRequestDTO> processVariablesListCC = new ArrayList<>();

    public CreditCommitteController(CreditCommitteServices creditCommitteServices) {
        this.creditCommitteServices = creditCommitteServices;
    }

    @GetMapping({"/credit-committee"})
    public String CreditCommitteView(Model model) throws IOException {
        List<String> processIds = this.creditCommitteServices.getAllProcessByActivityId("Activity_14mlhta");
        processVariablesListCC.clear();

        // Crear una lista para almacenar información de variables de proceso
        // Iterar a través de los processIds y obtener las variables para cada uno
        for (String processId : processIds) {
            CreditRequestDTO creditRequestDTO = this.creditCommitteServices.getProcessVariablesById(processId);
            TaskInfo taskInfo = this.creditCommitteServices.getTaskInfoByProcessId(processId);
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

    @PostMapping("/view-credit-committee")
    public  String viewTaskValidate(@RequestParam(name = "processId") String processId, Model model){
        CreditRequestDTO creditRequestDTO = this.creditCommitteServices.getProcessVariablesById(processId);
        TaskInfo taskInfo = this.creditCommitteServices.getTaskInfoByProcessId(processId);
        creditRequestDTO.setTaskInfo(taskInfo);
        model.addAttribute("creditRequestDTO", creditRequestDTO);
        model.addAttribute("titulo", "Application Review");

            return  "modals/Committee";
    }

    @PostMapping("/approve-credit-committee")
    public String approveTaskCouple(@RequestParam(name = "processId") String processId){
        this.creditCommitteServices.approveTask(processId);
        return "redirect:/credit-committee";
    }
}
