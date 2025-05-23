package com.server.dndserver.domain.elderly.service;

import com.server.dndserver.domain.elderly.domain.Elderly;
import com.server.dndserver.domain.elderly.repository.ElderlyRepository;
import com.server.dndserver.domain.elderly.dto.ElderlyRegisterRequest;
import com.server.dndserver.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ElderlyService {

    private final ElderlyRepository elderlyRepository;

    @Transactional
    public Elderly registerElderly(Member member, ElderlyRegisterRequest request) {

        Elderly elderly = Elderly.builder()
                .name(request.name())
                .birthDate(request.birthDate())
                .gender(request.gender())
                .phoneNumber(request.phoneNumber())
                .relationshipWithGuardian(request.relationship())
                .member(member)
                .build();

        member.completeFirstLogin();

        return elderlyRepository.save(elderly);
    }
}