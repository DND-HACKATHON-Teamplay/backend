package com.server.dndserver.domain.call.dto;

import com.server.dndserver.domain.call.domain.HealthStatus;
import com.server.dndserver.domain.call.domain.MindStatus;
import org.springframework.http.HttpStatus;

public record CallResponseDTO(Long id, HealthStatus healthStatus, MindStatus status, Long sleepHours) {
}
