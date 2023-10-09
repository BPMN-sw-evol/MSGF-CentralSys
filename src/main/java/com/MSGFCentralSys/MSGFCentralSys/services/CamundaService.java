package com.MSGFCentralSys.MSGFCentralSys.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class CamundaService {
    private final RestTemplate restTemplate;
    @Autowired
    public CamundaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> getAllProcess() throws IOException {
        String CAMUNDA_API_URL = "http://localhost:9000/engine-rest/process-instance?active=false&suspended=false&withIncident=false&withoutTenantId=false&processDefinitionWithoutTenantId=false&rootProcessInstances=false&leafProcessInstances=false&variableNamesIgnoreCase=false&variableValuesIgnoreCase=false";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(CAMUNDA_API_URL, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String responseBody = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            List<String> processIds = new ArrayList<>();

            for (JsonNode instanceNode : jsonNode) {
                String processId = instanceNode.get("id").asText();
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
}
