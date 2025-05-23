package com.server.dndserver.domain.conversation.service;

import com.server.dndserver.domain.call.dto.ConversationDTO;
import com.server.dndserver.domain.conversation.domain.Conversation;
import com.server.dndserver.domain.conversation.repository.ConversationRepository;
import com.server.dndserver.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;

    public List<ConversationDTO> getConversationsByCallDate(Member member, LocalDate date) {
        List<Conversation> conversations = conversationRepository.findByMemberIdAndCallDate(member.getId(), date);

        List<ConversationDTO> result = conversations.stream()
                .map(ConversationDTO::entityToDto)
                .collect(Collectors.toList());
        return result;
    }
}
