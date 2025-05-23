package com.server.dndserver.domain.conversation.service;

import com.server.dndserver.domain.call.dto.ConversationDTO;
import com.server.dndserver.domain.conversation.domain.Conversation;
import com.server.dndserver.domain.conversation.repository.ConversationRepository;
import com.server.dndserver.domain.elderly.domain.Elderly;
import com.server.dndserver.domain.elderly.repository.ElderlyRepository;
import com.server.dndserver.domain.member.domain.Member;
import com.server.dndserver.global.error.exception.BusinessException;
import com.server.dndserver.global.error.exception.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ElderlyRepository elderlyRepository;

    public List<ConversationDTO> getConversationsByCallDate(Member member, LocalDate date) {
        Elderly elderly = elderlyRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_ELDERLY_PERSONNEL));

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay().minusNanos(1);

        List<Conversation> conversations = conversationRepository.findByElderlyIdAndCallDate(
                elderly.getId(), start, end
        );

        if (conversations.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND_CONVERSATION);
        }

        return conversations.stream()
                .map(ConversationDTO::entityToDto)
                .collect(Collectors.toList());
    }
}
