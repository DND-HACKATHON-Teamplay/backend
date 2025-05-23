package com.server.dndserver.domain.call.service;

import com.server.dndserver.domain.call.domain.Call;
import com.server.dndserver.domain.call.dto.CallRequestDTO;
import com.server.dndserver.domain.call.repository.CallRepository;
import com.server.dndserver.domain.chatgpt.dto.RequestPromptDTO;
import com.server.dndserver.domain.chatgpt.service.ChatGptService;
import com.server.dndserver.domain.conversation.domain.Conversation;
import com.server.dndserver.domain.elderly.domain.Elderly;
import com.server.dndserver.domain.elderly.repository.ElderlyRepository;
import com.server.dndserver.global.error.exception.BusinessException;
import com.server.dndserver.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CallService {
    private final CallRepository callRepository;
    private final ElderlyRepository elderlyRepository;
    private final ChatGptService chatGptService;

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

    /*@Transactional
    public Long saveCall(CallRequestDTO dto) {

        Call call = Call.builder().build();
        dto.content().forEach(c ->
                call.addConversation(
                        new Conversation(c.content(), c.isElderly())
                )
        );
        Call saved = callRepository.saveAndFlush(call);

        String messages = dto.

        RequestPromptDTO gptReq = RequestPromptDTO.builder()
                .model("gpt-3.5-turbo")
                .messages(dto.content())
                .temperature(1)
                .max_tokens(50)
                .build();

        *//* 3) GPT 요청 생성 *//*
        RequestPromptDTO gptReq = new RequestPromptDTO(
                gptProperties.model(),   // ex) "gpt-4o-mini"
                messages,
                0.7                      // temperature
        );

        *//* 4) GPT 호출 및 결과 파싱 *//*
        Map<String, Object> gptRes = chatGptService.prompt(gptReq);

        // ─ 예: 첫 번째 choice 의 message.content 추출
        String gptSummary = ((Map<String, String>)
                ((Map<String, Object>)
                        ((List<?>) gptRes.get("choices")).get(0))
                        .get("message"))
                .get("content");

        *//* 5) 요약을 Call 엔티티에 저장(선택) *//*
        saved.setSummary(gptSummary);        // Call 엔티티에 summary 필드가 있다고 가정
        // flush 는 트랜잭션 종료 시 자동

        return saved.getId();
    }*/
}
