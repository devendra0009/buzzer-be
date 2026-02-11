package com.davendra.buzzer.dto.response;

import com.davendra.buzzer.entity.MessageModel;
import com.davendra.buzzer.entity.UserModel;
import com.davendra.buzzer.enums.ChatType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ChatResponse {
    private Long id;
    private String chatName;
    private String image;
    private ChatType chatType;
    private List<UserModel> users;
    private List<MessageModel> messages = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
