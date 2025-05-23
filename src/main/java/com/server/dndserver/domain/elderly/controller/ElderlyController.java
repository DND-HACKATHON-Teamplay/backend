package com.server.dndserver.domain.elderly.controller;

import com.server.dndserver.domain.elderly.domain.Elderly;
import com.server.dndserver.domain.elderly.dto.ElderlyRegisterRequest;
import com.server.dndserver.domain.elderly.service.ElderlyService;
import com.server.dndserver.domain.member.domain.Member;
import com.server.dndserver.global.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/elderly")
@RequiredArgsConstructor
public class ElderlyController {
    private final ElderlyService elderlyService;

    @PostMapping("/create")
    @Tag(name ="어르신 API")
    @Operation(summary = "어르신의 정보를 생성합니다, 요청 헤더에 포함된 JWT accessToken이 필요합니다.")
    public ResponseEntity<?> registerElderly(
            @AuthUser Member member, @RequestBody ElderlyRegisterRequest request) {

        Elderly elderly = elderlyService.registerElderly(member, request);

        return ResponseEntity.ok(Map.of("elderlyId", elderly.getId()));
    }


    @PatchMapping
    @Tag(name ="어르신 API")
    @Operation(summary = "어르신의 정보를 수정합니다, 요청 헤더에 포함된 JWT accessToken이 필요합니다.")
    public ResponseEntity<Object> updateElderly(@AuthUser Member member, @RequestBody ElderlyRegisterRequest request){
        elderlyService.updateElderly(member, request);
        return ResponseEntity.ok("어르신 정보 수정에 성공했습니다.");
    }

    @GetMapping
    @Tag(name ="어르신 API")
    @Operation(summary = "어르신의 정보를 받아옵니다. 요청 헤더에 포함된 JWT accessToken이 필요합니다.")
    public ResponseEntity<?> getElderly(@AuthUser Member member) {
        return ResponseEntity.ok(elderlyService.getElderlyById(member.getId()));
    }
}
