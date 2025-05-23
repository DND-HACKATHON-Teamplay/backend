package com.server.dndserver.domain.member.controller;

import com.server.dndserver.domain.member.domain.Member;
import com.server.dndserver.domain.member.service.LogoutService;
import com.server.dndserver.domain.member.service.MemberService;
import com.server.dndserver.global.annotation.AuthUser;
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
    public ResponseEntity<Void> deleteMember(@AuthUser Member member) {
        memberService.deleteMember(member);
        return ResponseEntity.ok().build();
    }
}
