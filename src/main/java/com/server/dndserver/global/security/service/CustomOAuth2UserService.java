package com.server.dndserver.global.security.service;

import com.server.dndserver.domain.member.domain.Member;
import com.server.dndserver.domain.member.domain.MemberRole;
import com.server.dndserver.domain.member.service.MemberService;
import com.server.dndserver.global.security.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = switch (registrationId) {
            case "google" -> new GoogleResponse(oAuth2User.getAttributes());
            case "kakao" -> new KakaoResponse(oAuth2User.getAttributes());
            default -> throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        };

        Member member = memberService.createMemberByOAuthInfo(oAuth2Response);

        MemberDto memberDto = new MemberDto();
        memberDto.setUsername(member.getId().toString());
        memberDto.setName(oAuth2Response.getName());
        memberDto.setEmail(oAuth2Response.getEmail());
        memberDto.setRole(MemberRole.USER.getValue());

        return new CustomOAuth2User(memberDto);
    }
}