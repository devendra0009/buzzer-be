package com.davendra.buzzer.dto.request;

import lombok.Data;

@Data
public class StoryRequest {
    private String captions;
    private String image;
    private Long userId;
    private Long deactivatedAt; // set after what seconds we want to deactive the story
}
