package com.server.dndserver.domain.member.service;

import com.server.dndserver.global.error.exception.ErrorCode;
import com.server.dndserver.global.security.exception.JwtInvalidException;
import com.server.dndserver.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    public void logout(String accessToken) {
        if (!jwtUtil.validateToken(accessToken)) {
            throw new JwtInvalidException(ErrorCode.INVALID_TOKEN.getMessage());
        }

        long expiration = jwtUtil.getTokenRemainingTime(accessToken);
        redisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
    }

    public boolean isLoggedOut(String token) {
        return redisTemplate.hasKey(token);
    }
}

