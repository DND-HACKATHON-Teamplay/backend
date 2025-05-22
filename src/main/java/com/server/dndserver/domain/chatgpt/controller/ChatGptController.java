package com.server.dndserver.domain.chatgpt.controller;

import com.server.dndserver.domain.chatgpt.dto.RequestPromptDTO;
import com.server.dndserver.domain.chatgpt.service.ChatGptService;
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
@RequestMapping("chatgpt")
@RequiredArgsConstructor
public class ChatGptController {
    private final ChatGptService chatGptService;

    @PostMapping("prompt")
    public ResponseEntity<Map<String, Object>> prompt(@RequestBody RequestPromptDTO request){
        log.info("[prompt] request.prompt = " + request.getMessages().get(0).getContent());
        Map<String, Object> result = chatGptService.prompt(request);
        return ResponseEntity.ok(result);
    }
}
