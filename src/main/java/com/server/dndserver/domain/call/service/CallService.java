package com.server.dndserver.domain.call.service;

import com.server.dndserver.domain.call.domain.Call;
import com.server.dndserver.domain.call.repository.CallRepository;
import com.server.dndserver.domain.elderly.domain.Elderly;
import com.server.dndserver.domain.elderly.repository.ElderlyRepository;
import com.server.dndserver.domain.member.domain.Member;
import com.server.dndserver.global.error.exception.BusinessException;
import com.server.dndserver.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CallService {
    private final CallRepository callRepository;
    private final ElderlyRepository elderlyRepository;


    public Map<LocalDate, Map<String, String>> getMonthlyEvaluationByMember(Long memberId, int year, int month) {
        Elderly elderly = elderlyRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_ELDERLY_PERSONNEL));

        List<Call> calls = callRepository.findByElderlyAndMonth(elderly.getId(), year, month);

        return calls.stream().collect(Collectors.toMap(
                call -> call.getCreatedDate().toLocalDate(),
                call -> Map.of(
                        "healthStatus", call.getHealthStatus().name(),
                        "mindStatus", call.getMindStatus().name()
                ),
                (existing, replacement) -> existing // 중복 날짜는 첫 값 유지
        ));
    }
}
