package com.server.dndserver.domain.call.service;

import com.server.dndserver.domain.call.domain.Call;
import com.server.dndserver.domain.call.dto.SummaryDTO;
import com.server.dndserver.domain.call.repository.CallRepository;
import com.server.dndserver.domain.conversation.domain.Conversation;
import com.server.dndserver.domain.conversation.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryService {
    private final CallRepository callRepository;
    private final ConversationRepository conversationRepository;
    public SummaryDTO getSummary(Long callId) {
        Call call = callRepository.findById(callId).orElseThrow(() -> new IllegalArgumentException("전화 조회 실패"));
        List<Conversation> conversations = conversationRepository.findByCallId(call.getId());
        return SummaryDTO.createFromCallAndConversationList(call, conversations);

    }
}
