package com.server.dndserver.domain.chatgpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.dndserver.domain.chatgpt.dto.RequestPromptDTO;
import com.server.dndserver.global.util.ChatGptUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGptService {
    private final ChatGptUtils chatGptUtils;
    @Value("${openai.api.url}")
    private String promptUrl;
    public Map<String, Object> prompt(RequestPromptDTO request) {
        log.info("promptUrl = " + promptUrl);

        HttpHeaders headers = chatGptUtils.httpHeaders();

        HttpEntity<RequestPromptDTO> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = chatGptUtils.restTemplate()
                .exchange(promptUrl, HttpMethod.POST, entity, String.class);

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
}
