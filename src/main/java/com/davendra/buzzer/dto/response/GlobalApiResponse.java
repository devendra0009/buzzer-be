package com.davendra.buzzer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalApiResponse<T> {
    private T data;
    private String message;
    private boolean status;
}
