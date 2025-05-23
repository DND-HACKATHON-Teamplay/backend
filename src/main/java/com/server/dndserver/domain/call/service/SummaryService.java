package com.server.dndserver.domain.call.service;

import com.server.dndserver.domain.call.domain.Call;
import com.server.dndserver.domain.call.dto.SummaryDTO;
import com.server.dndserver.domain.call.repository.CallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SummaryService {
    private final CallRepository callRepository;
    public SummaryDTO getSummary(Long callId) {
        Call call = callRepository.findById(callId).orElseThrow(() -> new IllegalArgumentException(""));
        return SummaryDTO.createFromCall(call);

    }
}
