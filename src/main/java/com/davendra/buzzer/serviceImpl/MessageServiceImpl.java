package com.davendra.buzzer.serviceImpl;

import com.davendra.buzzer.dto.request.MessageRequest;
import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.dto.response.MessageResponse;
import com.davendra.buzzer.dto.response.PageableResponse;
import com.davendra.buzzer.dto.response.StoryResponse;
import com.davendra.buzzer.entity.ChatModel;
import com.davendra.buzzer.entity.MessageModel;
import com.davendra.buzzer.entity.StoryModel;
import com.davendra.buzzer.entity.UserModel;
import com.davendra.buzzer.repositories.ChatRepo;
import com.davendra.buzzer.repositories.MessageRepo;
import com.davendra.buzzer.services.ChatService;
import com.davendra.buzzer.services.MessageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public MessageModel createMessage(UserModel user, Long chatId, MessageRequest messageRequest) {
        ChatModel chat = chatService.findChatById(chatId);
        MessageModel newMessage = new MessageModel();
        modelMapper.map(messageRequest, newMessage);
        newMessage.setChat(chat);
        newMessage.setUser(user);
        MessageModel savedMsg = messageRepo.save(newMessage);
        chat.getMessages().add(savedMsg);
        chatRepo.save(chat);
        return savedMsg;
    }

    @Override
    public GlobalApiResponse<?> findMessagesByChatId(Long chatId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<MessageModel> messageModelPage = messageRepo.findByChatId(chatId, pageable);

        List<MessageResponse> messageResponseList = messageModelPage.getContent()
                .stream()
                .map(msg -> modelMapper.map(msg, MessageResponse.class))
                .toList();

        PageableResponse<List<MessageResponse>> pageableResponse = new PageableResponse<>(
                messageResponseList,
                messageModelPage.getTotalPages(),
                messageModelPage.getNumber(),
                messageModelPage.getSize(),
                messageModelPage.getTotalElements(),
                messageModelPage.isLast()
        );

        return new GlobalApiResponse<>(pageableResponse, "All message of this chat retrieved successfully", true);
    }
}
