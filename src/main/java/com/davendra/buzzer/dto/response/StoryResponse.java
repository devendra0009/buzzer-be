package com.davendra.buzzer.dto.response;

import com.davendra.buzzer.entity.UserModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StoryResponse {
    private Long id;
    private String captions;
    private String image;
    private  boolean isActive;
    private UserModel user;
    private LocalDateTime deactivatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
