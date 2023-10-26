package com.MSGFCentralSys.MSGFCentralSys.controller;

import com.MSGFCentralSys.MSGFCentralSys.dto.CreditRequestDTO;
import com.MSGFCentralSys.MSGFCentralSys.dto.TaskInfo;
import com.MSGFCentralSys.MSGFCentralSys.services.CamundaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private final CamundaService camundaService;
    List<CreditRequestDTO> processVariablesList = new ArrayList<>();

    @Autowired
    public HomeController(CamundaService camundaService) {
        this.camundaService = camundaService;
    }

    @GetMapping({"/home","/"})
    public String mainView(Model model) {
        model.addAttribute("titulo","Welcome to the MsgFoundation's CENTRAL SYS");
        return "init";
    }

    @GetMapping("/CreditAnalyst")
    public String CreditAnalystView(Model model) throws IOException {

            List<String> processIds = this.camundaService.getAllProcessByAssignee("CreditAnalyst");
            List<String> taskInfoList = new ArrayList<>();
            processVariablesList.clear();
            // Iterar a través de los processIds y obtener las variables para cada uno
            for (String processId : processIds) {
                CreditRequestDTO creditRequestDTO = this.camundaService.getProcessVariablesById(processId);
                TaskInfo taskInfo = this.camundaService.getTaskInfoByProcessId(processId);
                creditRequestDTO.setTaskInfo(taskInfo);
                System.out.println("task info: "+taskInfo.toString());
                processVariablesList.add(creditRequestDTO);
            }

            // Agregar la lista de variables de proceso al modelo para pasarla a la vista
            model.addAttribute("processIds", processIds);
            model.addAttribute("processVariablesList", processVariablesList);
            model.addAttribute("titulo", "Analyze applications");
            return "views/CreditAnalyst";

    }

    @GetMapping({"/CreditCommitte"})
    public String CreditCommitteView(Model model) throws IOException{
        List<String> processIds = this.camundaService.getAllProcessByAssignee("CreditCommitte");
        processVariablesList.clear();

        // Crear una lista para almacenar información de variables de proceso
        // Iterar a través de los processIds y obtener las variables para cada uno
        for (String processId : processIds) {
            CreditRequestDTO creditRequestDTO = this.camundaService.getProcessVariablesById(processId);
            TaskInfo taskInfo = this.camundaService.getTaskInfoByProcessId(processId);
            creditRequestDTO.setTaskInfo(taskInfo);
            System.out.println("task info: "+taskInfo.toString());
            processVariablesList.add(creditRequestDTO);
        }


        // Agregar la lista de variables de proceso al modelo para pasarla a la vista
        model.addAttribute("processIds", processIds);
        model.addAttribute("processVariablesList", processVariablesList);
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


