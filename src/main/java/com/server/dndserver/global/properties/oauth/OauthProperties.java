package com.server.dndserver.global.properties.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth")
public record OauthProperties(
        String redirectUri
) {
}
