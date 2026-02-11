package com.davendra.buzzer.controller;

import com.davendra.buzzer.dto.request.MessageRequest;
import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.entity.MessageModel;
import com.davendra.buzzer.services.MessageService;
import com.davendra.buzzer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/message")
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;


    @PostMapping("/new/chat/{chatId}")
    public MessageModel createMessage(@RequestHeader("Authorization") String token, @PathVariable Long chatId, @RequestBody MessageRequest messageRequest) {
        return messageService.createMessage(userService.getUserFromToken(token), chatId, messageRequest);
    }

    @GetMapping("/get/chat/{chatId}")
    public ResponseEntity<GlobalApiResponse<?>> findMessagesByChatId(@PathVariable Long chatId, @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(messageService.findMessagesByChatId(chatId, page, size));
    }
}
