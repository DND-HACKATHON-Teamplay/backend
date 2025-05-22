package com.server.dndserver.global.security.jwt;

import com.server.dndserver.domain.member.domain.Member;
import com.server.dndserver.global.properties.jwt.JwtProperties;
import com.server.dndserver.global.security.token.JwtAuthenticationToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    public static final String TOKEN_ROLE_NAME = "role";
    private final JwtProperties jwtProperties;

    public String generateAccessToken(String memberId, String role) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + jwtProperties.accessTokenExpirationMilliTime());
        return Jwts.builder()
                .setIssuer(jwtProperties.issuer())
                .setSubject(memberId)
                .claim(TOKEN_ROLE_NAME, role)
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .signWith(getAccessTokenKey())
                .compact();
    }

    public String generateRefreshToken(Member member) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + jwtProperties.refreshTokenExpirationMilliTime());

        return Jwts.builder()
                .setIssuer(jwtProperties.issuer())
                .setSubject(member.getId().toString())
                .claim(TOKEN_ROLE_NAME, member.getRole().getValue())
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .signWith(getRefreshTokenKey())
                .compact();
    }

    public Claims getAccessTokenClaims(Authentication authentication) {

        return Jwts.parserBuilder()
                .requireIssuer(jwtProperties.issuer())
                .setSigningKey(getAccessTokenKey())
                .build()
                .parseClaimsJws(((JwtAuthenticationToken) authentication).getJsonWebToken())
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getAccessTokenKey())
                    .requireIssuer(jwtProperties.issuer())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long getTokenRemainingTime(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getAccessTokenKey())
                .requireIssuer(jwtProperties.issuer())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }

    private Key getAccessTokenKey() {
        return Keys.hmacShaKeyFor(jwtProperties.accessTokenSecret().getBytes());
    }

    private Key getRefreshTokenKey() {
        return Keys.hmacShaKeyFor(jwtProperties.refreshTokenSecret().getBytes());
    }


}
