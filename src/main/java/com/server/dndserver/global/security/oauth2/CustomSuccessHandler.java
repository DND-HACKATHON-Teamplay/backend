package com.server.dndserver.global.security.oauth2;

import com.server.dndserver.domain.member.service.MemberService;
import com.server.dndserver.global.properties.oauth.OauthProperties;
import com.server.dndserver.global.security.dto.CustomOAuth2User;
import com.server.dndserver.global.security.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final OauthProperties oauthProperties;
    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String accessToken = jwtUtil.generateAccessToken(customUserDetails.getUsername(), role);

        String redirectUrl = oauthProperties.redirectUri()
                + "?accessToken="
                + accessToken;

        // Redirect to the constructed URL
        response.sendRedirect(redirectUrl);
        System.out.println("redirectUrl: " + redirectUrl);
        System.out.println("accessToken: " + accessToken);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
