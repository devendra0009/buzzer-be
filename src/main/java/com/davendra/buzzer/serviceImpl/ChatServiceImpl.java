package com.davendra.buzzer.serviceImpl;

import com.davendra.buzzer.dto.response.ChatResponse;
import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.dto.response.PageableResponse;
import com.davendra.buzzer.dto.response.StoryResponse;
import com.davendra.buzzer.entity.ChatModel;
import com.davendra.buzzer.entity.StoryModel;
import com.davendra.buzzer.entity.UserModel;
import com.davendra.buzzer.enums.ChatType;
import com.davendra.buzzer.repositories.ChatRepo;
import com.davendra.buzzer.services.ChatService;
import com.davendra.buzzer.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ChatModel createChat(Long user1, List<Long> users2) throws Exception {

        UserModel userModel1 = userService.findUserById(user1);

        List<UserModel> userModel2 = userService.findAllUsersById(users2);
        userModel2.add(userModel1);
        List<Long> allUsersId = userModel2.stream().map(item -> {
            return item.getId();
        }).toList();

        // check if the chat exist or not with given users
        ChatModel isExist = chatRepo.findChatByExactUserIds(allUsersId);

        if (isExist != null) {
            return isExist;
        }

        // create new chat
        ChatModel newChat = new ChatModel();
        // handle for group chat
        if (users2.size() > 1) {
            newChat.setChatType(ChatType.GROUP);
        } else {
            newChat.setChatType(ChatType.PRIVATE);
            if (Objects.equals(user1, users2.getFirst())) {
                throw new Exception("Can't chat with yourself for now!");
            }
        }
        // add all users in the chat
        newChat.getUsers().addAll(userModel2);

        // save the chat
        return chatRepo.save(newChat);
    }

    @Override
    public ChatModel findChatById(Long chatId) {
        return chatRepo.findById(chatId).orElseThrow(() -> new NoSuchElementException("Chat not found with id " + chatId));
    }

    @Override
    public GlobalApiResponse<?> findChatsByUserId(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ChatModel> chatModelPage = chatRepo.findByUsersId(userId, pageable);

        List<ChatResponse> chatResponseList = chatModelPage.getContent()
                .stream()
                .map(chat -> modelMapper.map(chat, ChatResponse.class))
                .toList();

        PageableResponse<List<ChatResponse>> pageableResponse = new PageableResponse<>(
                chatResponseList,
                chatModelPage.getTotalPages(),
                chatModelPage.getNumber(),
                chatModelPage.getSize(),
                chatModelPage.getTotalElements(),
                chatModelPage.isLast()
        );

        return new GlobalApiResponse<>(pageableResponse, "Chats retrieved successfully", true);
    }
}
