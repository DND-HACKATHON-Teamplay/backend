package com.server.dndserver.global.config.properties;

import com.server.dndserver.global.properties.gpt.GptProperties;
import com.server.dndserver.global.properties.jwt.JwtProperties;
import com.server.dndserver.global.properties.oauth.OauthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({
        JwtProperties.class,
        OauthProperties.class,
        GptProperties.class
})
@Configuration
public class PropertiesConfig {

}