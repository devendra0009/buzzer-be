package com.davendra.buzzer.dto.response;

import com.davendra.buzzer.entity.ChatModel;
import com.davendra.buzzer.entity.UserModel;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class MessageResponse {
    private Long id;
    private String content;
    private String image;
    private String audio;
    private UserModel user;
    private ChatModel chat;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
