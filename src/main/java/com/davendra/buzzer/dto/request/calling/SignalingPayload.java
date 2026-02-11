package com.davendra.buzzer.dto.request.calling;

import lombok.Data;

@Data
public class SignalingPayload {
    private String type;
    private String from;
    private String to;
    private Object offer;
    private Object answer;
    private Object candidate;
    private Boolean muted;
}
