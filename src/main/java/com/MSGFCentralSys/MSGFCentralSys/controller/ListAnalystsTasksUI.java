package com.MSGFCentralSys.MSGFCentralSys.controller;

import org.springframework.web.bind.annotation.GetMapping;

// diseño ???
// API first -- piense como quiere construir el código
//           -- cuales son las anotaciones, los métodos, ...


// Clase representa una pantalla
@BPMNSwimLane("analyst")
@BPMNUSerTask("....")
@BPMBReadVariable({"...", "..."})
@BPMBUpdateVariable({"...", "..."})
public class ListAnalystsTasksUI extends BPMUI {

    @BPMNVariable("nombreSolicitante")
    String nombre;

    public void initVariables() {
        nombre = instanciaDeProceso.getVariable("nombre");
    }

    // un mapping que muestra la pantalla
    @InitUI
    @GetMapping("/analyst/listTasks")
    public void verPantalla(String idInstanciaProceso, String idINstanciaTarea) {
        this.initUI(idInstanciaProceso, idINstanciaTarea);

        // instancia de proceso
        // instancia de la tarea

        // Consultas sobre todos los procesos 
        apiProcesos.getTareasPendientesPara(userId);

        return "vistaListaTareas";

    }

    // otros mapping que representan respuestas a eventos 
    @ActionListenerUI(boton="Grabar")
    @PostMapping("/analyst/ListaTasks/grabar")
    pubic void grabar() {

        // ...

        instanciaProceso.setVariables(..);
        instanciaProceso.completeTask("ok");

        redirect(...);

    }

    @ActionListenerUI(boton="Cancelar")
    @PostMapping("/analyst/ListaTasks/cancelar")
    public void cancelar() {

        instanciaProceso.setVariables(..);
        instanciaProceso.completeTask("cancelar");

        redirect(...);
    }



    
}
