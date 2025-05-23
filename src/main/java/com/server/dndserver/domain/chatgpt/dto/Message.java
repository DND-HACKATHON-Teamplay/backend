package com.server.dndserver.domain.chatgpt.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@Setter
@Builder
@AllArgsConstructor
public class Message {
    private String role;
    private String content;
}
