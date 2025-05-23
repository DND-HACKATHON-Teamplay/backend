package com.server.dndserver.domain.call.dto;

import com.server.dndserver.domain.call.domain.Call;
import com.server.dndserver.domain.call.domain.HealthStatus;
import com.server.dndserver.domain.call.domain.MindStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SummaryDTO{
    private MindStatus mindStatus;
    private Long sleepHours;
    private HealthStatus healthStatus;
    private String summary;

    public static SummaryDTO createFromCall(Call call){
        return SummaryDTO.builder()
                .mindStatus(call.getMindStatus())
                .sleepHours(call.getSleepTime())
                .healthStatus(call.getHealthStatus())
                .build();
    }
}
