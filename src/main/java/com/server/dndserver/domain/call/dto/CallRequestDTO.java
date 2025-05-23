package com.server.dndserver.domain.call.dto;

import com.server.dndserver.domain.call.domain.HealthStatus;
import com.server.dndserver.domain.call.domain.MindStatus;

import java.util.List;

public record CallRequestDTO
        (
                List<ConversationDTO> content
        ) {
}
