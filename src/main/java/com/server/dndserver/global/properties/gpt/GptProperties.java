package com.server.dndserver.global.properties.gpt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openai")
public record GptProperties(String key, String url) {
}
