package com.server.dndserver.domain.member.controller;

import com.server.dndserver.domain.member.domain.Member;
import com.server.dndserver.domain.member.service.LogoutService;
import com.server.dndserver.domain.member.service.MemberService;
import com.server.dndserver.global.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @DeleteMapping
    @Tag(name = "유저 API")
    @Operation(summary = "회원 탈퇴를 진행합니다. 요청 헤더에 포함된 JWT Access Token이 필요합니다.")
    public ResponseEntity<Void> deleteMember(@AuthUser Member member) {
        memberService.deleteMember(member);
        return ResponseEntity.ok().build();
    }
}
