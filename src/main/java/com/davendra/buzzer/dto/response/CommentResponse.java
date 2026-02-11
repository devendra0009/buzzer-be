package com.davendra.buzzer.dto.response;

import com.davendra.buzzer.entity.PostModel;
import com.davendra.buzzer.entity.UserModel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private UserModel commentedBy;
    private List<UserModel> likedBy;
    private PostModel post;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
