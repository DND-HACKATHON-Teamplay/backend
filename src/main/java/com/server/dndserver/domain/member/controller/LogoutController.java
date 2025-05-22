package com.server.dndserver.domain.member.controller;

import com.server.dndserver.domain.member.service.LogoutService;
import com.server.dndserver.global.error.exception.ErrorCode;
import com.server.dndserver.global.security.exception.JwtInvalidException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class LogoutController {
    private final LogoutService logoutService;

    @PostMapping("/logout")
    @Tag(name = "로그아웃 API")
    @Operation(summary = "요청 헤더에 포함된 JWT Access Token을 블랙리스트에 등록하여 로그아웃 처리합니다.")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String accessToken = authorization.substring(7);
            logoutService.logout(accessToken);
            return ResponseEntity.ok("로그아웃 성공");
        }
        throw new JwtInvalidException(ErrorCode.ACCESS_DENIED.getMessage());
    }
}
