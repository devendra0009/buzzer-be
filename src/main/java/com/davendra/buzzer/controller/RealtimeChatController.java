package com.davendra.buzzer.controller;

import com.davendra.buzzer.dto.request.calling.SignalingPayload;
import com.davendra.buzzer.entity.MessageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class RealtimeChatController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/{groupId}") // here user can send the data from client side
//    public MessageModel sendToUser(@Payload MessageModel messageModel, @DestinationVariable String groupId) {
    public void sendToUser(@Payload MessageModel messageModel, @DestinationVariable String groupId) {
        log.info("msg: {}", messageModel);
        log.info(groupId);
        log.info("other user: {}", messageModel.getChat().getUsers().get(1).getUserName());
        simpMessagingTemplate.convertAndSend("/user/" + groupId + "/private", messageModel);
//        simpMessagingTemplate.convertAndSendToUser(messageModel.getChat().getUsers().get(1).getUserName(), "/user/" + groupId + "/private", messageModel); // provide user, destination and payload
//        return messageModel; // i would've returned if it has a @SendTo queue defined
    }

    @MessageMapping("/call/audio/{groupId}") // client publishes to /app/call/12
    public void handleCall(SignalingPayload payload, @DestinationVariable String groupId) {
        // Forward to recipient queue
        // recipient listens on /
        simpMessagingTemplate.convertAndSend("/calling/audio" + groupId, payload);
    }
    @MessageMapping("/call/video/{groupId}") // client publishes to /app/call/12
    public void handleVideoCall(SignalingPayload payload, @DestinationVariable String groupId) {
        // Forward to recipient queue
        // recipient listens on /
        simpMessagingTemplate.convertAndSend("/calling/video" + groupId, payload);
    }
}
