package com.server.dndserver.domain.call.dto;

import com.server.dndserver.domain.conversation.domain.Conversation;

public record ConversationDTO(
        boolean is_elderly,
        String conversation
) {
    public static ConversationDTO entityToDto(Conversation conversation){
        return new ConversationDTO(conversation.isElderly(), conversation.getContent());
    }
}
