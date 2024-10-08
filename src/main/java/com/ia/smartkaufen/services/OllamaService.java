package com.ia.smartkaufen.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class OllamaService {

    private final RestTemplate restTemplate;

    public String sendDataToServer(String data) {
        String url = "http://127.0.0.1:11434/api/generate"; // Ajusta la URL según sea necesario
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("model", "llama3:8b-instruct-q8_0");
        requestBodyMap.put("prompt", data);
        requestBodyMap.put("format","json");
        requestBodyMap.put("stream", false);

        String requestBody;

        try {
            requestBody = objectMapper.writeValueAsString(requestBodyMap);
            System.out.println("Request Body: " + requestBody);
        } catch (JsonProcessingException e) {
            return "Error al convertir el cuerpo de la petición a JSON: " + e.getMessage();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            String responseBody = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();
            Map<String, Object> responseMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
            return responseMap.get("response").toString();
        } catch (HttpClientErrorException e) {
            System.err.println("Error: " + e.getResponseBodyAsString());
            return "Error: " + e.getResponseBodyAsString();
        } catch (JsonProcessingException e) {
            return "Error al procesar la respuesta de la IA: " + e.getMessage();
        }
    }
}

