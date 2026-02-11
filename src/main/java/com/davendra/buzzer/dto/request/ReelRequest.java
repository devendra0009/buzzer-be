package com.davendra.buzzer.dto.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class ReelRequest {
    private String title;
    private MultipartFile video; // change this multipart afterwards
    private String description;
}
