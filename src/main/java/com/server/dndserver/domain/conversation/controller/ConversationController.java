package com.server.dndserver.domain.conversation.controller;

import com.server.dndserver.domain.conversation.domain.Conversation;
import com.server.dndserver.domain.conversation.service.ConversationService;
import com.server.dndserver.domain.member.domain.Member;
import com.server.dndserver.global.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/conversation")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @GetMapping("/by-call-date")
    @Tag(name = "대화 API")
    @Operation(summary = "날짜별 대화를 가져옵니다. JWT accessToken이 필요합니다.")
    public ResponseEntity<List<Conversation>> getConversationsByCallDate(
            @AuthUser Member member,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(conversationService.getConversationsByCallDate(member, date));
    }
}
