package com.MSGFCentralSys.MSGFCentralSys.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AnalystService {

    private final RestTemplate restTemplate;

    @Autowired
    public AnalystService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getTasksForProcessDefinition() {
        // Definir la URL de la solicitud GET
        String camundaUrl = "http://localhost:9000/engine-rest/task?processDefinitionId=MSGF-Test%3A1%3Acdb13fbd-64ce-11ee-9380-0242ac1d0002&withoutTenantId=false&includeAssignedTasks=false&assigned=false&unassigned=false&withoutDueDate=false&withCandidateGroups=false&withoutCandidateGroups=false&withCandidateUsers=false&withoutCandidateUsers=false&active=false&suspended=false&variableNamesIgnoreCase=false&variableValuesIgnoreCase=false";

        // Realizar la solicitud GET y obtener la respuesta como una cadena
        String responseBody = restTemplate.getForObject(camundaUrl, String.class);

        if (responseBody != null) {
            System.out.println("Respuesta de Camunda: " + responseBody);

            // Puedes procesar la respuesta JSON y extraer los datos que necesitas aquí si es necesario

            return responseBody;
        } else {
            System.err.println("No se pudo obtener una respuesta de Camunda.");
            return null;
        }
    }


}
