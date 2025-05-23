package com.server.dndserver.domain.call.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.dndserver.domain.call.domain.Call;
import com.server.dndserver.domain.call.domain.HealthStatus;
import com.server.dndserver.domain.call.domain.MindStatus;
import com.server.dndserver.domain.call.dto.CallRequestDTO;
import com.server.dndserver.domain.call.dto.CallResponseDTO;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

        Optional<Elderly> byPhoneNumber = elderlyRepository.findByPhoneNumber("01094601439");
        Call call = Call.builder()
                .elderly(byPhoneNumber.orElseThrow(() -> new BusinessException(ErrorCode.NOT_ELDERLY_PERSONNEL)))
                .build();

        content.content().forEach(c ->
                call.addConversation(new Conversation(c.conversation(), c.is_elderly()))
        );
        Call saved = callRepository.saveAndFlush(call);   // 영속 상태가 됨

        String messages = joinElderlyContents(content);

        List<Message> messageList = List.of(
                Message.builder()
                        .role("system")
                        .content("""
                            다음 대화에서 어르신의 상태를
                            "healthStatus": "HAPPY|NORMAL|BAD",
                            "sleepTime": 7,
                            "mindStatus": "HAPPY|NORMAL|BAD"
                            형식의 JSON만 응답해줘.
                            """)
                        .build(),
                Message.builder()
                        .role("user")
                        .content(messages)
                        .build()
        );

        log.info("messageList = " + messages);


        RequestPromptDTO gptReq = RequestPromptDTO.builder()
                .model("gpt-4")
                .messages(messageList)
                .temperature(1)
                .max_tokens(50)
                .build();

        /* 2. GPT 호출 */
        Map<String, Object> gptRes = chatGptService.prompt(gptReq);

        /* 3. choices[0].message.conversation 에서 JSON 파싱 */
        String raw = Optional.ofNullable(gptRes.get("choices"))
                .map(List.class::cast)
                .filter(list -> !list.isEmpty())
                .map(list -> (Map<?, ?>) list.get(0))
                .map(map -> (Map<?, ?>) map.get("message"))
                .map(msg -> (String) msg.get("content"))   // ★ content 로 변경
                .orElseThrow(() -> new IllegalStateException("GPT 응답 형식 오류"))
                .trim();

        String json = raw.replaceAll("```[\\s\\S]*?```", "")
                .replaceAll("(?i)^json\\s*\\{", "{") // "json{" → "{"
                .trim();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);

        /* 4. 값 매핑 → 엔티티 세팅 */
        HealthStatus hs   = HealthStatus.valueOf(node.get("healthStatus").asText());
        Long          slp  = node.get("sleepTime").asLong();
        MindStatus   mind = MindStatus.valueOf(node.get("mindStatus").asText());

        saved.updateStatus(hs, slp, mind);
        return new CallResponseDTO(saved.getId(), hs, mind, slp);
    }

    private String joinElderlyContents(CallRequestDTO dto) {
        return dto.content().stream()                 // List<ConversationDTO>
                .filter(ConversationDTO::is_elderly)   // isElderly == true
                .map(ConversationDTO::conversation)        // 대화 문자열만
                .collect(Collectors.joining(" "));    // 공백 기준으로 합치기
    }
}
