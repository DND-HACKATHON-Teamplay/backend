package com.server.dndserver.domain.call.controller;

import com.server.dndserver.domain.call.dto.SummaryDTO;
import com.server.dndserver.domain.call.service.SummaryService;
import com.server.dndserver.domain.member.domain.Member;
import com.server.dndserver.global.annotation.AuthUser;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SummaryController {
    private final SummaryService summaryService;
    @PostMapping("call/{callId}/summary")
    public ResponseEntity<Object> getSummary(@RequestParam Long conversationId){
        SummaryDTO summaryDTO = summaryService.getSummary(conversationId);
        return ResponseEntity.ok(summaryDTO);
    }
}
