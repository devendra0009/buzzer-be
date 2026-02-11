package com.davendra.buzzer.services;

import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.entity.ChatModel;

import java.util.List;

public interface ChatService {
    public ChatModel createChat(Long user1, List<Long> users2) throws Exception; // user1 is current user logged in, user2 is the user which we want to get connected with

    public ChatModel findChatById(Long chatId);

    public GlobalApiResponse<?> findChatsByUserId(Long userId, int page, int size);
}
