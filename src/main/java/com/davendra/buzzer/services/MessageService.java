package com.davendra.buzzer.services;

import com.davendra.buzzer.dto.request.MessageRequest;
import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.entity.MessageModel;
import com.davendra.buzzer.entity.UserModel;

import java.util.List;

public interface MessageService {
    public MessageModel createMessage(UserModel user, Long chatId, MessageRequest messageRequest);

    public GlobalApiResponse<?> findMessagesByChatId(Long chatId, int page, int size);
}
