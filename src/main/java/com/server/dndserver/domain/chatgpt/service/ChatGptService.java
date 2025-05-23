package com.server.dndserver.domain.chatgpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.dndserver.domain.chatgpt.dto.RequestPromptDTO;
import com.server.dndserver.global.properties.gpt.GptProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGptService {
    private final GptProperties gptProperties;

    public Map<String, Object> prompt(RequestPromptDTO request) {
        log.info("promptUrl = " + gptProperties.url());

        HttpHeaders headers = httpHeaders();

        HttpEntity<RequestPromptDTO> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate()
                .exchange(gptProperties.url(), HttpMethod.POST, entity, String.class);

        Map<String, Object> resultMap = null;

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            resultMap = objectMapper.readValue(response.getBody(), new TypeReference<>() {
            });
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return resultMap;
    }

    private RestTemplate restTemplate(){
        return new RestTemplate();
    }

    private HttpHeaders httpHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(gptProperties.key());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
