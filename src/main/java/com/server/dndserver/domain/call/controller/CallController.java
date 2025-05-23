package com.server.dndserver.domain.call.controller;

import com.server.dndserver.domain.call.dto.CallRequestDTO;
import com.server.dndserver.domain.call.dto.CallResponseDTO;
import com.server.dndserver.domain.call.service.CallService;
import com.server.dndserver.domain.member.domain.Member;
import com.server.dndserver.global.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/call")
@RequiredArgsConstructor
public class CallController {

    private final CallService callService;

    @Tag(name = "전화 API")
    @Operation(summary = "어르신의 달 별 전화 평가를 반환합니다. JWT accessToken이 필요합니다.")
    @GetMapping("/elderly/{elderlyId}")
    public Map<LocalDate, Map<String, String>> getCallsByMonth(
            @AuthUser Member member,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return callService.getMonthlyEvaluationByMember(member.getId(), year, month);
    }

   /* @PostMapping
    public ResponseEntity<CallResponseDTO> createCall(
            @RequestBody CallRequestDTO req) {

        Long id = callService.saveCall(req);
        return ResponseEntity.ok(new IdResponse(id));
    }
*/

}
