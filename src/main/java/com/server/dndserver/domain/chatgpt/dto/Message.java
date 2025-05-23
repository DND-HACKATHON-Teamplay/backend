package com.server.dndserver.domain.chatgpt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Message {
    private String role;
    private String content;
}
