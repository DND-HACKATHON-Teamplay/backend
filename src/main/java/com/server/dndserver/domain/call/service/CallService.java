package com.server.dndserver.domain.call.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.dndserver.domain.call.domain.Call;
import com.server.dndserver.domain.call.domain.HealthStatus;
import com.server.dndserver.domain.call.domain.MindStatus;
import com.server.dndserver.domain.call.dto.CallRequestDTO;
import com.server.dndserver.domain.call.dto.CallResponseDTO;
import com.server.dndserver.domain.call.dto.CallStatusDTO;
import com.server.dndserver.domain.call.dto.ConversationDTO;
import com.server.dndserver.domain.call.repository.CallRepository;
import com.server.dndserver.domain.chatgpt.dto.Message;
import com.server.dndserver.domain.chatgpt.dto.RequestPromptDTO;
import com.server.dndserver.domain.chatgpt.service.ChatGptService;
import com.server.dndserver.domain.conversation.domain.Conversation;
import com.server.dndserver.domain.elderly.domain.Elderly;
import com.server.dndserver.domain.elderly.repository.ElderlyRepository;
import com.server.dndserver.global.error.exception.BusinessException;
import com.server.dndserver.global.error.exception.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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

    @Transactional
    public CallResponseDTO saveCall(CallRequestDTO content) throws JsonProcessingException {

        Elderly elderly = elderlyRepository.findByPhoneNumber("01094601439")
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_ELDERLY_PERSONNEL));

        Call call = Call.builder()
                .elderly(elderly)
                .build();

        content.content().forEach(c ->
                call.addConversation(new Conversation(c.conversation(), c.is_elderly()))
        );
        Call saved = callRepository.save(call);      // save-only → flush 는 커밋 시 자동

        /* 1. GPT 프롬프트 구성 -------------------------------------------------- */
        String messages = joinElderlyContents(content);

        List<Message> messageList = List.of(
                Message.builder()
                        .role("system")
                        .content("""
                        절대 아무 말도 하지 말고, 다음 대화에서 어르신의 상태를
                        {"healthStatus":"HAPPY|NORMAL|BAD","sleepTime":7,"mindStatus":"HAPPY|NORMAL|BAD"}
                        형식의 JSON 한 줄만 응답해줘.
                        """)
                        .build(),
                Message.builder()
                        .role("user")
                        .content(messages)
                        .build()
        );

        RequestPromptDTO gptReq = RequestPromptDTO.builder()
                .model("gpt-4o-mini")          // 가능한 최신·저가 모델로
                .messages(messageList)
                .temperature(1)              // JSON 출력 안정화
                .max_tokens(50)
                .build();

        /* 2. GPT 호출 ----------------------------------------------------------- */
        Map<String, Object> gptRes = chatGptService.prompt(gptReq);

        /* 3. GPT 응답에서 JSON만 추출 ------------------------------------------- */
        String raw = Optional.ofNullable(gptRes.get("choices"))
                .map(List.class::cast)
                .filter(list -> !list.isEmpty())
                .map(list -> (Map<?, ?>) list.get(0))
                .map(map -> (Map<?, ?>) map.get("message"))
                .map(msg -> (String) msg.get("content"))
                .orElseThrow(() -> new BusinessException(ErrorCode.GPT_EMPTY_RESPONSE))
                .trim();

        // === 여기부터 교체된 부분 =============================================
        int left  = raw.indexOf('{');
        int right = raw.lastIndexOf('}');
        if (left == -1 || right == -1 || left >= right) {
            log.error("GPT 원문 = {}", raw);
            throw new BusinessException(ErrorCode.GPT_EMPTY_RESPONSE);
        }
        String json = raw.substring(left, right + 1);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        try {
            node = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            log.error("GPT JSON 파싱 실패, 원문 = {}", json);
            throw new BusinessException(ErrorCode.GPT_EMPTY_RESPONSE);
        }
        // ======================================================================

        /* 4. 값 매핑 → 엔티티 업데이트 ------------------------------------------ */
        HealthStatus hs  = HealthStatus.valueOf(node.get("healthStatus").asText());
        long         slp = node.get("sleepTime").asLong();
        MindStatus   md  = MindStatus.valueOf(node.get("mindStatus").asText());

        saved.updateStatus(hs, slp, md);
        return new CallResponseDTO(saved.getId(), hs, md, slp);
    }


    public CallStatusDTO getDailyCall(Long memberId, LocalDate date) {
        Elderly elderly = elderlyRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_ELDERLY_PERSONNEL));

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.plusDays(1).atStartOfDay().minusNanos(1);

        Call call = callRepository
                .findFirstByElderlyIdAndCreatedDateBetweenOrderByCreatedDateDesc(elderly.getId(), start, end)
                .orElseThrow(() -> new EntityNotFoundException("해당 날짜 통화 기록 없음"));

        return new CallStatusDTO(
                call.getHealthStatus(),
                call.getSleepTime(),
                call.getMindStatus()
        );
    }

    private String joinElderlyContents(CallRequestDTO dto) {
        return dto.content().stream()                 // List<ConversationDTO>
                .filter(ConversationDTO::is_elderly)   // isElderly == true
                .map(ConversationDTO::conversation)        // 대화 문자열만
                .collect(Collectors.joining(" "));    // 공백 기준으로 합치기
    }
}
