package com.davendra.buzzer.dto.request;

import lombok.Data;

@Data
public class MessageRequest {
    private String content;
    private String image;
    private String audio;
}
