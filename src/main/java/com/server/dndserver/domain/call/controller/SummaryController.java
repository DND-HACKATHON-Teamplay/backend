package com.server.dndserver.domain.call.controller;

import com.server.dndserver.domain.call.dto.SummaryDTO;
import com.server.dndserver.domain.call.service.SummaryService;
import com.server.dndserver.domain.member.domain.Member;
import com.server.dndserver.global.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/call")
@RequiredArgsConstructor
public class SummaryController {
    private final SummaryService summaryService;
    @PostMapping("{callId}/summary")
    @Tag(name ="요약 조회 API")
    @Operation(summary = "디테일 뷰에서 대화 내용 요약과 전체 대화를 조회하는 API. JWT 토큰이 필요합니다.")
    public ResponseEntity<Object> getSummary(@RequestParam Long conversationId){
        SummaryDTO summaryDTO = summaryService.getSummary(conversationId);
        return ResponseEntity.ok(summaryDTO);
    }


}
