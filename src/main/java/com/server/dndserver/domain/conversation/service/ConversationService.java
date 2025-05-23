package com.server.dndserver.domain.conversation.service;

import com.server.dndserver.domain.conversation.domain.Conversation;
import com.server.dndserver.domain.conversation.repository.ConversationRepository;
import com.server.dndserver.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;

    public List<Conversation> getConversationsByCallDate(Member member, LocalDate date) {
        return conversationRepository.findByMemberIdAndCallDate(member.getId(), date);
    }
}
