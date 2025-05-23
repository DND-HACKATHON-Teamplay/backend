package com.server.dndserver.domain.chatgpt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RequestPromptDTO {
    private String model;
    private List<Message> messages;
    private int temperature;
    private int max_tokens;


}
