package com.server.dndserver.domain.chatgpt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RequestPromptDTO {
    private String model;
    private List<Message> messages;
    private int temperature;
    private int max_tokens;


}
