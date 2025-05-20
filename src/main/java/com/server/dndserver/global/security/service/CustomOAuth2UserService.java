package com.server.dndserver.global.security.service;

import com.server.dndserver.domain.member.domain.Member;
import com.server.dndserver.domain.member.domain.MemberRole;
import com.server.dndserver.domain.member.service.MemberService;
import com.server.dndserver.global.security.dto.CustomOAuth2User;
import com.server.dndserver.global.security.dto.GoogleResponse;
import com.server.dndserver.global.security.dto.MemberDto;
import com.server.dndserver.global.security.dto.OAuth2Response;
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
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {

            return null;
        }

        Member member = memberService.createMemberByOAuthInfo(oAuth2Response);

        MemberDto memberDto = new MemberDto();
        memberDto.setUsername(member.getId().toString());
        memberDto.setName(oAuth2Response.getName());
        memberDto.setRole(MemberRole.USER.getValue());

        return new CustomOAuth2User(memberDto);

    }
}