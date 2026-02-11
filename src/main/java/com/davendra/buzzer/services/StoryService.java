package com.davendra.buzzer.services;

import com.davendra.buzzer.dto.request.StoryRequest;
import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.dto.response.StoryResponse;

import java.util.List;

public interface StoryService {
    public StoryResponse createStory(StoryRequest story);
    public GlobalApiResponse<?> getAllStoryForUser(Long userId, int page, int size);
    public GlobalApiResponse<?> getAllStoryOfUser(Long userId, int page, int size);

    public StoryResponse storySeenByUser(Long storyId, Long userId);
}
