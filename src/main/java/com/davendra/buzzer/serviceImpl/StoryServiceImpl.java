package com.davendra.buzzer.serviceImpl;

import com.davendra.buzzer.dto.request.StoryRequest;
import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.dto.response.PageableResponse;
import com.davendra.buzzer.dto.response.StoryResponse;
import com.davendra.buzzer.entity.StoryModel;
import com.davendra.buzzer.entity.UserModel;
import com.davendra.buzzer.repositories.StoryRepo;
import com.davendra.buzzer.repositories.UserRepo;
import com.davendra.buzzer.services.StoryService;
import com.davendra.buzzer.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StoryServiceImpl implements StoryService {

    @Autowired
    private StoryRepo storyRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public StoryResponse createStory(StoryRequest storyRequest) {
        UserModel user = userService.findUserById(storyRequest.getUserId());
        StoryModel story = modelMapper.map(storyRequest, StoryModel.class);
        story.setId(null);
        story.setUser(user);
        story.setActive(true);
        story.setDeactivatedAt(LocalDateTime.now().plusSeconds(storyRequest.getDeactivatedAt() == null ? 20 : storyRequest.getDeactivatedAt()));  // add the story deactivated time , default 20 swconds
        return modelMapper.map(storyRepo.save(story), StoryResponse.class);
    }

    @Override
    public GlobalApiResponse<?> getAllStoryForUser(Long userId, int page, int size) {

        UserModel userModel = userService.findUserById(userId);
        List<Long> usersToSearchStoriesFor = new ArrayList<>(userModel.getFollowings());
        usersToSearchStoriesFor.add(userModel.getId());

        Pageable pageable = PageRequest.of(page, size);
        Page<StoryModel> storyModelPage = storyRepo.findActiveByUserIdIn(usersToSearchStoriesFor, pageable);

        List<StoryResponse> storyResponseList = storyModelPage.getContent()
                .stream()
                .map(story -> modelMapper.map(story, StoryResponse.class))
                .toList();

        PageableResponse<List<StoryResponse>> pageableResponse = new PageableResponse<>(
                storyResponseList,
                storyModelPage.getTotalPages(),
                storyModelPage.getNumber(),
                storyModelPage.getSize(),
                storyModelPage.getTotalElements(),
                storyModelPage.isLast()
        );

        return new GlobalApiResponse<>(pageableResponse, "User stories retrieved successfully", true);
    }

    @Override
    public GlobalApiResponse<?> getAllStoryOfUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StoryModel> storyModelPage = storyRepo.findActiveByUserId(userId, pageable);

        List<StoryResponse> storyResponseList = storyModelPage.getContent()
                .stream()
                .map(story -> modelMapper.map(story, StoryResponse.class))
                .toList();

        PageableResponse<List<StoryResponse>> pageableResponse = new PageableResponse<>(
                storyResponseList,
                storyModelPage.getTotalPages(),
                storyModelPage.getNumber(),
                storyModelPage.getSize(),
                storyModelPage.getTotalElements(),
                storyModelPage.isLast()
        );

        return new GlobalApiResponse<>(pageableResponse, "User's stories retrieved successfully", true);
    }

    @Override
    public StoryResponse storySeenByUser(Long storyId, Long userId) {
        StoryModel storyModel = storyRepo.findById(storyId).orElseThrow(() -> new NoSuchElementException("No story found with id " + storyId));
        UserModel userModel = userRepo.findById(userId).orElseThrow(() -> new NoSuchElementException("No user found with id " + userId));
        storyModel.getSeenBy().add(userModel);
        return modelMapper.map(storyRepo.save(storyModel), StoryResponse.class);
    }

    @Scheduled(fixedRate = 60000) // Runs every minute; adjust as needed
    public void deactivateExpiredStories() {
        System.out.println("expiring stories...");
        List<StoryModel> storiesToDeactivate = storyRepo.findByIsActiveTrueAndDeactivateAtBefore(LocalDateTime.now());
        if (storiesToDeactivate.isEmpty()) return;
        storiesToDeactivate.forEach(story -> {
            story.setActive(false);
        });
        storyRepo.saveAll(storiesToDeactivate);
    }
}
