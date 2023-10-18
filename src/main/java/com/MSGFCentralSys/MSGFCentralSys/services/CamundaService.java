package com.MSGFCentralSys.MSGFCentralSys.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CamundaService {
    private final RestTemplate restTemplate;
    @Value("${camunda.url.start}")
    private String camundaStartUrl;
    @Autowired
    public CamundaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> getAllProcessByAssignee(String assignee) throws IOException {
        String CAMUNDA_API_URL = "http://localhost:9000/engine-rest/task?withoutTenantId=false&assignee="+assignee+"&includeAssignedTasks=false&assigned=false&unassigned=false&withoutDueDate=false&withCandidateGroups=false&withoutCandidateGroups=false&withCandidateUsers=false&withoutCandidateUsers=false&active=false&suspended=false&variableNamesIgnoreCase=false&variableValuesIgnoreCase=false&sortBy=created&sortOrder=asc";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(CAMUNDA_API_URL, String.class);
        //

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String responseBody = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            List<String> processIds = new ArrayList<>();

            for (JsonNode instanceNode : jsonNode) {
                String processId = instanceNode.get("processInstanceId").asText();
                processIds.add(processId);
            }

            return processIds;
        } else {
            System.err.println("Error al obtener las instancias de proceso: " + responseEntity.getStatusCode());
            return new ArrayList<>();
        }
    }

    public Map<String, Object> getProcessVariablesById(String processId) {
        String CAMUNDA_API_URL = "http://localhost:9000/engine-rest/";
        String camundaURL = CAMUNDA_API_URL + "process-instance/" + processId + "/variables?deserializeValues=true";

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                camundaURL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> variablesMap = responseEntity.getBody();

        if (variablesMap != null) {
            return variablesMap;
        } else {
            return Collections.emptyMap(); // Devolver un mapa vacío si no se encontraron variables
        }
    }

    public void setAssignee(String taskId, String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", userId);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        String camundaUrl = "http://localhost:9000/engine-rest/task/" + taskId + "/assignee";

        try {
            ResponseEntity<String> response = restTemplate.exchange(camundaUrl, HttpMethod.POST, requestEntity, String.class);
            System.out.println("Assignee set successfully");
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            System.err.println("Error in the Camunda request: " + errorMessage);
        }
    }

    public Map<String, String> getTaskInfoByProcessId(String processId) {
        // Construir la URL para consultar las tareas relacionadas con el proceso
        String camundaUrl = "http://loca" +
                "lhost:9000/engine-rest/task?processInstanceId=" + processId;

        try {
            // Realizar una solicitud GET a Camunda para obtener la lista de tareas
            ResponseEntity<List<Map>> response = restTemplate.exchange(camundaUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map>>() {});

            // Verificar si la respuesta contiene tareas
            List<Map> tasks = response.getBody();
            if (tasks != null && !tasks.isEmpty()) {
                // Supongamos que tomamos la primera tarea encontrada
                Map<String, String> taskInfo = new HashMap<>();
                taskInfo.put("taskId", String.valueOf(tasks.get(0).get("id")));
                taskInfo.put("taskName", String.valueOf(tasks.get(0).get("name")));
                //taskInfo.put("assignee", String.valueOf(tasks.get(0).get("assignee")));

                return taskInfo;
            } else {
                System.err.println("No tasks found for Process ID " + processId);
                return null;
            }
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            System.err.println("Error en la solicitud a Camunda: " + errorMessage);
            return null;
        }
    }

    public String getTaskIdByProcessId(String processId) {
        Map<String, String> taskInfo = getTaskInfoByProcessId(processId);
        if (taskInfo != null) {
            return taskInfo.get("taskId");
        } else {
            return null;
        }
    }

    public String getTaskNameByProcessId(String processId) {
        Map<String, String> taskInfo = getTaskInfoByProcessId(processId);
        if (taskInfo != null) {
            return taskInfo.get("taskName");
        } else {
            return null;
        }
    }

    public String assignVariablesTask(Boolean isValid){

        // Construir el cuerpo de la solicitud para Camunda
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        // Crear un mapa para los atributos que deseas enviar
        Map<String, Object> variables = new HashMap<>();
        variables.put("allFine", Map.of("value", isValid, "type", "Boolean"));

        // Crear el cuerpo de la solicitud
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("variables", variables);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // Realizar la solicitud POST a Camunda
            ResponseEntity<Map> response = restTemplate.postForEntity(camundaStartUrl, requestEntity, Map.class);
            return "";
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            return null;
        }
    }

    public String completeTask(String processId, String assignee, Boolean isValid) {
        // Obtener la información de la tarea a partir del Process ID
        Map<String, String> taskInfo =  getTaskInfoByProcessId(processId);
        System.out.println("1. "+processId+ " 2. "+assignee+" 3. "+ isValid);
        if (taskInfo != null) {
            // Extraer el Task ID de la información de la tarea
            String taskId = taskInfo.get("taskId");
            // Construir el cuerpo de la solicitud para Camunda
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            System.out.println("taskid"+taskId);
            // Crear el cuerpo de la solicitud
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("allFine", Map.of("value", isValid, "type", "Boolean"));
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // Realizar la solicitud POST a Camunda
            String camundaUrl = "http://localhost:9000/engine-rest/task/" + taskId + "/complete";
            try {
                // Realizar la solicitud POST a Camunda
                ResponseEntity<Map> response = restTemplate.postForEntity(camundaUrl, requestEntity, Map.class);
                String taskId1 = getTaskIdByProcessId(processId);
                setAssignee(taskId1,assignee);

                return String.valueOf(response);
            } catch (HttpClientErrorException e) {
                String errorMessage = e.getResponseBodyAsString();
                System.err.println("Error en la solicitud a Camunda: " + errorMessage);
                return null;
            }
        } else {
            System.err.println("No se pudo obtener información de la tarea para Process ID " + processId);
            return null;
        }
    }
}
