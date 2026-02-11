package com.davendra.buzzer.controller;

import com.davendra.buzzer.dto.request.ChatRequest;
import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.entity.ChatModel;
import com.davendra.buzzer.services.ChatService;
import com.davendra.buzzer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserService userService;

    @PostMapping("/new")
    public ChatModel createChat(@RequestHeader("Authorization") String token, @RequestBody ChatRequest chatRequest) throws Exception {
        System.out.println(token);
        return chatService.createChat(userService.getUserFromToken(token).getId(), chatRequest.getUsers2());
    }

    @GetMapping("/get/{chatId}")
    public ChatModel getChatById(@PathVariable Long chatId) {
        return chatService.findChatById(chatId);
    }

    @GetMapping("/user/get")
    public ResponseEntity<GlobalApiResponse<?>> findChatsByUserId(@RequestHeader("Authorization") String token, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(chatService.findChatsByUserId(userService.getUserFromToken(token).getId(), page, size));
    }

}
