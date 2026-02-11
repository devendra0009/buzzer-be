package com.davendra.buzzer.dto.response;

import com.davendra.buzzer.entity.UserModel;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class ReelResponse {
    private Long id;
    private String title;
    private String video;
    private String description;
    private UserModel user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
