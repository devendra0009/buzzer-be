package com.davendra.buzzer.controller;


import com.davendra.buzzer.dto.request.ReelRequest;
import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.entity.ReelModel;
import com.davendra.buzzer.services.ReelsService;
import com.davendra.buzzer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reel")
public class ReelsController {

    @Autowired
    private ReelsService reelsService;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ReelModel> createReels(@RequestHeader("Authorization") String token,
                                                 @RequestParam(name = "title", required = false) String title,
                                                 @RequestParam(name = "description", required = false) String desc,
                                                 @RequestPart("file") MultipartFile file) throws IOException {
        ReelRequest reelRequest = ReelRequest.builder().video(file).title(title).description(desc).build();
        return ResponseEntity.ok(reelsService.createReel(reelRequest, userService.getUserFromToken(token).getId()));
    }

    @GetMapping("/all")
    public ResponseEntity<GlobalApiResponse<?>> getAllReels(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reelsService.getAllReels(page, size));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReelModel>> getReelByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(reelsService.getReelByUserId(userId));
    }
}
