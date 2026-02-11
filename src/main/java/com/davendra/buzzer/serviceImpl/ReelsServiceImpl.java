package com.davendra.buzzer.serviceImpl;

import com.davendra.buzzer.dto.request.ReelRequest;
import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.dto.response.PageableResponse;
import com.davendra.buzzer.dto.response.ReelResponse;
import com.davendra.buzzer.entity.ReelModel;
import com.davendra.buzzer.repositories.ReelsRepo;
import com.davendra.buzzer.services.CloudinaryUploadService;
import com.davendra.buzzer.services.ReelsService;
import com.davendra.buzzer.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ReelsServiceImpl implements ReelsService {

    @Autowired
    private ReelsRepo reelsRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private CloudinaryUploadService cloudinaryUploadService;

    @Override
    public ReelModel createReel(ReelRequest reelRequest, Long userId) throws IOException {
        ReelModel reel = new ReelModel();
        modelMapper.map(reelRequest, reel);
        // here error may occur
        reel.setUser(userService.findUserById(userId));
        reel.setVideo(cloudinaryUploadService.uploadFile(reelRequest.getVideo()));
        return reelsRepo.save(reel);
    }

    @Override
    public GlobalApiResponse<?> getAllReels(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ReelModel> reelModelPage = reelsRepo.findAll(pageable);

        // Convert List<PostModel> to List<PostResponse>
        List<ReelResponse> reelResponses = reelModelPage.getContent()
                .stream()
                .map(post -> modelMapper.map(post, ReelResponse.class))
                .toList();
        // Create PageableResponse
        PageableResponse<List<ReelResponse>> pageableResponse = new PageableResponse<>(
                reelResponses,
                reelModelPage.getTotalPages(),
                reelModelPage.getNumber(),
                reelModelPage.getSize(),
                reelModelPage.getTotalElements(),
                reelModelPage.isLast()
        );
        return new GlobalApiResponse<>(
                pageableResponse,
                "Reels retrieved successfully",
                true
        );
    }

    @Override
    public List<ReelModel> getReelByUserId(Long userId) {
        userService.findUserById(userId);
        return reelsRepo.findByUserId(userId);
    }
}
