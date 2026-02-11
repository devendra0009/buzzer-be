package com.davendra.buzzer.dto.response;

import com.davendra.buzzer.entity.CommentModel;
import com.davendra.buzzer.entity.UserModel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostResponse {
    private Long id;
    private String caption;
    private List<String> mediaFiles;
    private UserModel user;
    private List<CommentModel> comments;
    private List<String> tags;
    private String location;
    private List<UserModel> usersTagged;
    private List<UserModel> likedBy;
    private List<UserModel> savedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
