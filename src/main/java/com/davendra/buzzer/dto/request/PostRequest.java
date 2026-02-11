package com.davendra.buzzer.dto.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class PostRequest {
    private String caption;
    private Long userId;
    private List<String> tags;
    private String location;
    private List<Long> usersTagged;
    private List<MultipartFile> mediaFiles;
}
