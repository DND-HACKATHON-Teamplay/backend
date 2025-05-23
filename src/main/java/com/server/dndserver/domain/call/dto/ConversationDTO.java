package com.server.dndserver.domain.call.dto;

public record ConversationDTO(
        boolean isElderly,
        String content
) {
}
