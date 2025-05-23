package com.server.dndserver.domain.member.service;

import com.server.dndserver.domain.call.repository.CallRepository;
import com.server.dndserver.domain.elderly.domain.Elderly;
import com.server.dndserver.domain.elderly.repository.ElderlyRepository;
import com.server.dndserver.domain.elderly.service.ElderlyService;
import com.server.dndserver.domain.member.domain.Member;
import com.server.dndserver.domain.member.domain.MemberRole;
import com.server.dndserver.domain.member.repository.MemberRepository;
import com.server.dndserver.global.error.exception.ErrorCode;
import com.server.dndserver.global.error.exception.NotFoundException;
import com.server.dndserver.global.security.dto.OAuth2Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ElderlyRepository elderlyRepository;
    private final CallRepository callRepository;

    @Transactional(readOnly = true)
    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Member createMemberByOAuthInfo(OAuth2Response oauthInfo) {
        if(memberRepository.existsByEmail(oauthInfo.getEmail())) {
            return getMemberByEmail(oauthInfo.getEmail());
        }

        Member member = Member.createDefaultMember(oauthInfo.getName(), MemberRole.USER,
                oauthInfo.getEmail());

        return memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Member member) {
        List<Elderly> elderlyList = elderlyRepository.findAllByMember(member);

        for (Elderly elderly : elderlyList) {
            callRepository.deleteAllByElderly(elderly);
        }

        elderlyRepository.deleteAll(elderlyList);
        memberRepository.delete(member);
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
