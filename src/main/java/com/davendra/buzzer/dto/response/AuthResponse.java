package com.davendra.buzzer.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class AuthResponse {
    private String token;
    private String message;

}
