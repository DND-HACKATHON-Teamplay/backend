package com.server.dndserver.domain.call.dto;

import com.server.dndserver.domain.call.domain.Call;
import com.server.dndserver.domain.call.domain.HealthStatus;
import com.server.dndserver.domain.call.domain.MindStatus;
import com.server.dndserver.domain.conversation.domain.Conversation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SummaryDTO{
    private MindStatus mindStatus;
    private Long sleepHours;
    private HealthStatus healthStatus;
    private String summary;
    private List<Conversation> content;

    public static SummaryDTO createFromCallAndConversationList(Call call, List<Conversation> conversations){
        return SummaryDTO.builder()
                .mindStatus(call.getMindStatus())
                .sleepHours(call.getSleepTime())
                .healthStatus(call.getHealthStatus())
                .content(conversations)
                .build();
    }
}
