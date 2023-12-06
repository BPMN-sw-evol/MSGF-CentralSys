package com.MSGFCentralSys.MSGFCentralSys.services;

import com.msgfoundation.annotations.*;
import com.MSGFCentralSys.MSGFCentralSys.dto.CreditRequestDTO;
import com.MSGFCentralSys.MSGFCentralSys.dto.TaskInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Service
@BPMNTask(type = "UserTask",name = {"Revisar información pareja"})
public class CreditAnalystCoupleServices {
    private final RestTemplate restTemplate;
    private List<TaskInfo> tasksList = new ArrayList<>();
    @BPMNGetVariables(variables = "1")
    public String variable1;

    @Autowired
    public CreditAnalystCoupleServices(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @BPMNGetterVariables(variables = "Processes Instances")
    public List<String> getAllProcessByActivityId(String activityId) {
        String url = "http://localhost:9000/engine-rest/history/activity-instance?sortBy=startTime&sortOrder=desc&activityId=" + activityId + "&finished=false&unfinished=true&withoutTenantId=false";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        List<String> processIds = new ArrayList<>();

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String responseBody = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                for (JsonNode node : jsonNode) {
                    String processInstanceId = node.get("processInstanceId").asText();
                    processIds.add(processInstanceId);
                }
            } catch (IOException e) {
                System.err.println("Error al analizar la respuesta JSON: " + e.getMessage());
            }
        } else {
            System.err.println("Error al obtener las instancias de proceso: " + responseEntity.getStatusCode());
        }

        return processIds;
    }

    @BPMNGetterVariables(variables = "CreditRequestDTO")
    @BPMNContainer(variables="....")
    public CreditRequestDTO getProcessVariablesById(String processId) {
        String CAMUNDA_API_URL = "http://localhost:9000/engine-rest/";
        String camundaURL = CAMUNDA_API_URL + "process-instance/" + processId + "/variables?deserializeValues=true";

        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                camundaURL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );

        Map<String, Object> variablesMap = responseEntity.getBody();

        if (variablesMap != null) {
            CreditRequestDTO creditRequest = new CreditRequestDTO();
            Map<String, Object> coupleName1Map = (Map<String, Object>) variablesMap.get("coupleName1");
            String coupleName1Value = (String) coupleName1Map.get("value");
            creditRequest.setCoupleName1(coupleName1Value);

            Map<String, Object> coupleName2Map = (Map<String, Object>) variablesMap.get("coupleName2");
            String coupleName2Value = (String) coupleName2Map.get("value");
            creditRequest.setCoupleName2(coupleName2Value);

            Map<String, Object> marriageYearsMap = (Map<String, Object>) variablesMap.get("marriageYears");
            Integer marriageYearsValue = (Integer) marriageYearsMap.get("value");
            creditRequest.setMarriageYears(marriageYearsValue.longValue());

            Map<String, Object> bothEmployeesMap = (Map<String, Object>) variablesMap.get("bothEmployees");
            Boolean bothEmployeesValue = (Boolean) bothEmployeesMap.get("value");
            creditRequest.setBothEmployees(bothEmployeesValue);

            Map<String, Object> housePricesMap = (Map<String, Object>) variablesMap.getOrDefault("housePrices", Collections.singletonMap("value", 0));
            Integer housePricesValue = (Integer) housePricesMap.get("value");
            creditRequest.setHousePrices(housePricesValue != null ? housePricesValue.longValue() : 0);

            Map<String, Object> quotaValueMap = (Map<String, Object>) variablesMap.getOrDefault("quotaValue", Collections.singletonMap("value", 0));
            Integer quotaValueValue = (Integer) quotaValueMap.get("value");
            creditRequest.setQuotaValue(quotaValueValue != null ? quotaValueValue.longValue() : 0);

            Map<String, Object> coupleSavingsMap = (Map<String, Object>) variablesMap.getOrDefault("coupleSavings", Collections.singletonMap("value", 0));
            Integer coupleSavingsValue = (Integer) coupleSavingsMap.get("value");
            creditRequest.setCoupleSavings(coupleSavingsValue != null ? coupleSavingsValue.longValue() : 0);

            Map<String, Object> requestDateMap = (Map<String, Object>) variablesMap.get("creationDate");
            String requestDateValue = (String) requestDateMap.get("value");
            creditRequest.setRequestDate(requestDateValue);

            Map<String, Object> coupleEmail1Map = (Map<String, Object>) variablesMap.get("coupleEmail1");
            String coupleEmail1Value = (String) coupleEmail1Map.get("value");
            creditRequest.setCoupleEmail1(coupleEmail1Value);

            Map<String, Object> coupleEmail2Map = (Map<String, Object>) variablesMap.get("coupleEmail2");
            String coupleEmail2Value = (String) coupleEmail2Map.get("value");
            creditRequest.setCoupleEmail2(coupleEmail2Value);

            Map<String, Object> countReviewsCSMap = (Map<String, Object>) variablesMap.getOrDefault("countReviewsBpm", Collections.singletonMap("value", 0));
            Integer countReviewsCSValue = (Integer) countReviewsCSMap.get("value");
            creditRequest.setCountReviewsCS(countReviewsCSValue != null ? countReviewsCSValue.longValue() : 0);

            creditRequest.setProcessId(processId);
            return creditRequest;
        } else {
            return null; // Devolver null si no se encontraron variables
        }
    }

    @BPMNSetterVariables(variables = "assignee")
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

    @BPMNGetterVariables(variables = "TaskInfo")
    public TaskInfo getTaskInfoByProcessId(String processId) {
        // Construir la URL para consultar las tareas relacionadas con el proceso
        String camundaUrl = "http://localhost:9000/engine-rest/task?processInstanceId=" + processId;

        try {
            // Realizar una solicitud GET a Camunda para obtener la lista de tareas
            ResponseEntity<List<Map>> response = restTemplate.exchange(camundaUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map>>() {
            });

            // Verificar si la respuesta contiene tareas
            List<Map> tasks = response.getBody();
            if (tasks != null && !tasks.isEmpty()) {
                // Supongamos que tomamos la primera tarea encontrada
                Map<String, String> taskInfoMap = new HashMap<>();
                taskInfoMap.put("taskId", String.valueOf(tasks.get(0).get("id")));
                taskInfoMap.put("taskName", String.valueOf(tasks.get(0).get("name")));
                taskInfoMap.put("assignee", String.valueOf(tasks.get(0).get("assignee")));

                System.out.println("Task Info for Process ID " + processId + ":");
                System.out.println("Task ID: " + taskInfoMap.get("taskId"));
                System.out.println("Task Name: " + taskInfoMap.get("taskName"));
                System.out.println("Assignee: " + taskInfoMap.get("assignee"));

                TaskInfo taskInfo = new TaskInfo();
                taskInfo.setProcessId(processId);
                taskInfo.setTaskId(taskInfoMap.get("taskId"));
                taskInfo.setTaskName(taskInfoMap.get("taskName"));
                taskInfo.setTaskAssignee(taskInfoMap.get("assignee"));

                tasksList.add(taskInfo);
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

    @BPMNGetterVariables(variables = "taskId")
    public String getTaskIdByProcessIdWithApi(String processId) {
        String camundaUrl = "http://localhost:9000/engine-rest/task?processInstanceId=" + processId;

        try {
            ResponseEntity<List<Map>> response = restTemplate.exchange(camundaUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map>>() {
            });
            List<Map> tasks = response.getBody();

            if (tasks != null && !tasks.isEmpty()) {
                return String.valueOf(tasks.get(0).get("id"));
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
    @BPMNGetterVariables(variables = "taskName")
    public String getTaskNameByProcessId(String processId) {
        for (TaskInfo taskInfo : tasksList) {
            if (taskInfo.getProcessId().equals(processId)) {
                return taskInfo.getTaskName();
            }
        }
        return null;
    }
    @BPMNSetterVariables(variables = "taskInfo")
    public void updateTaskByProcessId(String processId, String taskId) {
        for (TaskInfo taskInfo : tasksList) {
            if (taskInfo.getProcessId().equals(processId)) {
                taskInfo.setTaskId(taskId);
            }
        }
    }

    @BPMNSetterVariables(variables = "allFine")
    public String approveTask(String processId) {

        TaskInfo taskInfo = getTaskInfoByProcessId(processId);

        if (taskInfo != null) {
            String taskId = taskInfo.getTaskId();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> variables = new HashMap<>();
            Map<String, Object> allFine = new HashMap<>();
            allFine.put("value", true);
            allFine.put("type", "Boolean");
            variables.put("allFine", allFine);
            requestBody.put("variables", variables);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            try (Connection connection = DriverManager.getConnection("jdbc:postgresql://rds-msgf.cyrlczakjihy.us-east-1.rds.amazonaws.com:5432/credit_request", "postgres", "msgfoundation")) {
                String camundaUrl = "http://localhost:9000/engine-rest/task/" + taskId + "/complete";
                restTemplate.postForEntity(camundaUrl, requestEntity, Map.class);
                String newTaskId = getTaskIdByProcessIdWithApi(processId);

                if (newTaskId != null) {
                    updateTaskByProcessId(processId, newTaskId);
                    setAssignee(newTaskId, "CreditAnalyst");
                }
                return "";
            } catch (SQLException | HttpClientErrorException e) {
                System.err.println("Error during task completion: " + e.getMessage());
                return null;
            }
        } else {
            System.err.println("No task information found for Process ID " + processId);
            return null;
        }
    }

    @BPMNSetterVariables(variables = "allFine")
    public String rejectTask(String processId) {
        TaskInfo taskInfo = getTaskInfoByProcessId(processId);

        if (taskInfo != null) {
            String taskId = taskInfo.getTaskId();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> variables = new HashMap<>();
            Map<String, Object> allFine = new HashMap<>();
            allFine.put("value", false);
            allFine.put("type", "Boolean");
            variables.put("allFine", allFine);
            requestBody.put("variables", variables);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            try (Connection connection = DriverManager.getConnection("jdbc:postgresql://rds-msgf.cyrlczakjihy.us-east-1.rds.amazonaws.com:5432/credit_request", "postgres", "msgfoundation")) {
                String camundaUrl = "http://localhost:9000/engine-rest/task/" + taskId + "/complete";
                restTemplate.postForEntity(camundaUrl, requestEntity, Map.class);
                String newTaskId = getTaskIdByProcessIdWithApi(processId);

                if (newTaskId != null) {
                    // Update the "status" to "Draft" in the "CreditRequest" table
                    String updateStatusQuery = "UPDATE credit_request SET status = 'DRAFT', count_reviewcr = count_reviewcr + 1 WHERE process_id = ?";
                    try (PreparedStatement statement = connection.prepareStatement(updateStatusQuery)) {
                        statement.setString(1, processId);
                        updateTaskByProcessId(processId, newTaskId);
                        setAssignee(newTaskId, "MarriedCouple");
                    } catch (SQLException e) {
                        return null;
                    }
                }
                return "";
            } catch (SQLException | HttpClientErrorException e) {
                System.err.println("Error during task completion: " + e.getMessage());
                return null;
            }
        } else {
            System.err.println("No task information found for Process ID " + processId);
            return null;
        }
    }
}
