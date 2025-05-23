package com.server.dndserver.domain.call.dto;


import com.server.dndserver.domain.call.domain.HealthStatus;
import com.server.dndserver.domain.call.domain.MindStatus;

public record CallStatusDTO(HealthStatus healthStatus, Long sleepTime, MindStatus mindStatus) {
}
