package com.server.dndserver.domain.chatgpt.controller;

import com.server.dndserver.domain.chatgpt.dto.RequestPromptDTO;
import com.server.dndserver.domain.chatgpt.service.ChatGptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/chatgpt")
@RequiredArgsConstructor
public class ChatGptController {
    private final ChatGptService chatGptService;

    @PostMapping("prompt")
    @Tag(name = "지피티 API")
    @Operation(summary = "챗지피티 API")
    public ResponseEntity<Map<String, Object>> prompt(@RequestBody RequestPromptDTO request){
        log.info("[prompt] request.prompt = " + request.getMessages().get(0).getContent());
        Map<String, Object> result = chatGptService.prompt(request);
        return ResponseEntity.ok(result);
    }
}
