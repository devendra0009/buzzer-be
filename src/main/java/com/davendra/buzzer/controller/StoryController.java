package com.davendra.buzzer.controller;


import com.davendra.buzzer.dto.request.StoryRequest;
import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.dto.response.StoryResponse;
import com.davendra.buzzer.services.StoryService;
import com.davendra.buzzer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/story")
public class StoryController {
    @Autowired
    private StoryService storyService;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<StoryResponse> createStory(@RequestBody StoryRequest storyRequest, @RequestHeader("Authorization") String token) {
        storyRequest.setUserId(userService.getUserFromToken(token).getId());
        StoryResponse createdStory = storyService.createStory(storyRequest);
        return ResponseEntity.ok(createdStory);
    }


    @GetMapping("/for/user")
    public ResponseEntity<GlobalApiResponse<?>> getAllStoryForUser(@RequestHeader("Authorization") String token, @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size) {
        Long userId = userService.getUserFromToken(token).getId();
        return ResponseEntity.ok(storyService.getAllStoryForUser(userId, page, size));
    }

    @GetMapping("/of/user/{userId}")
    public ResponseEntity<GlobalApiResponse<?>> getAllStoryOfUser(@PathVariable Long userId, @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(storyService.getAllStoryOfUser(userId, page, size));
    }

    // will optimize it -> seen by story srchitecture
    @PutMapping("/user/seen/update/{storyId}")
    public ResponseEntity<StoryResponse> updateStorySeenByUser(@PathVariable Long storyId, @RequestHeader("Authorization") String token) {
        return ResponseEntity.of(Optional.ofNullable(storyService.storySeenByUser(storyId, userService.getUserFromToken(token).getId())));
    }
}
