package com.davendra.buzzer.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ChatRequest {
    // user1 will come from auth token
    private List<Long> users2;
}
