package com.davendra.buzzer.serviceImpl;

import com.davendra.buzzer.dto.request.PostRequest;
import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.dto.response.PageableResponse;
import com.davendra.buzzer.dto.response.PostResponse;
import com.davendra.buzzer.entity.PostModel;
import com.davendra.buzzer.entity.UserModel;
import com.davendra.buzzer.repositories.PostRepo;
import com.davendra.buzzer.repositories.UserRepo;
import com.davendra.buzzer.services.CloudinaryUploadService;
import com.davendra.buzzer.services.PostService;
import com.davendra.buzzer.services.UserService;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepo postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CloudinaryUploadService cloudinaryUploadService;


    @Override
    public PostResponse createPost(PostRequest postReq, Long userId) throws IOException {
        UserModel user = userService.findUserById(userId);
        PostModel postModel = new PostModel();
        modelMapper.map(postReq, postModel);

        // here error may occur
        postModel.setUser(user);
        if (postReq.getUsersTagged() != null && !postReq.getUsersTagged().isEmpty()) {
            List<UserModel> taggedUesrs = userService.findAllUsersById(postReq.getUsersTagged()); // Assuming it fetches users based on the IDs
            postModel.setUsersTagged(taggedUesrs);
        }

        // get image and video link
        if (postReq.getMediaFiles() != null && !postReq.getMediaFiles().isEmpty()) {
            // get image and video link only if media files exist
            List<String> urls = cloudinaryUploadService.uploadMultipleFiles(postReq.getMediaFiles());
            postModel.setMediaFiles(urls);
        }

        PostModel savedPost = postRepository.save(postModel);
        PostResponse postResponse = new PostResponse();
        modelMapper.map(savedPost, postResponse);
        return postResponse;
    }

    @Override
    public String deletePost(Long postId, Long userId) throws Exception {
        PostModel post = findPostsById(postId);
        UserModel user = userService.findUserById(userId);
        if (!Objects.equals(post.getUser().getId(), user.getId())) {
            throw new Exception("You can only delete your posts!");
        }
        postRepository.delete(post);
        return "Post deleted successfully";
    }

    @Override
    public GlobalApiResponse<?> findPostsByUserId(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PostModel> postModelPage = postRepository.findByUserId(userId, pageable);

        // Convert List<PostModel> to List<PostResponse>
        List<PostResponse> postResponses = postModelPage.getContent()
                .stream()
                .map(post -> modelMapper.map(post, PostResponse.class))
                .toList();
        // Create PageableResponse
        PageableResponse<List<PostResponse>> pageableResponse = new PageableResponse<>(
                postResponses,
                postModelPage.getTotalPages(),
                postModelPage.getNumber(),
                postModelPage.getSize(),
                postModelPage.getTotalElements(),
                postModelPage.isLast()
        );
        return new GlobalApiResponse<>(
                pageableResponse,
                "Posts for user retrieved successfully",
                true
        );
    }

    @Override
    public PostModel findPostsById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    @Override
    public GlobalApiResponse<PageableResponse<List<PostResponse>>> findAllPost(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostModel> postModelPage = postRepository.findAll(pageable);

        // Convert List<PostModel> to List<PostResponse>
        List<PostResponse> postResponses = postModelPage.getContent()
                .stream()
                .map(post -> modelMapper.map(post, PostResponse.class))
                .toList();
        // Create PageableResponse
        PageableResponse<List<PostResponse>> pageableResponse = new PageableResponse<>(
                postResponses,
                postModelPage.getTotalPages(),
                postModelPage.getNumber(),
                postModelPage.getSize(),
                postModelPage.getTotalElements(),
                postModelPage.isLast()
        );
        return new GlobalApiResponse<>(
                pageableResponse,
                "Posts retrieved successfully",
                true
        );
    }

    @Override
    public PostResponse likePost(Long userId, Long postId) {
        PostModel post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        UserModel user = userService.findUserById(userId);

        // just stored ids of user for now, whenever we want to see the users who had liked the post, we can call an api and pass this getLikedBy array
        if (post.getLikedBy().contains(user)) {
            post.getLikedBy().remove(user);
        } else {
            post.getLikedBy().add(user);
        }
        PostModel savedPost = postRepository.save(post);
        PostResponse postResponse = new PostResponse();
        modelMapper.map(savedPost, postResponse);
        return postResponse;
    }

    @Override
    public PostResponse savePost(Long userId, Long postId) {
        PostModel post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        UserModel user = userService.findUserById(userId);

        if (post.getSavedBy().contains(user)) {
            post.getSavedBy().remove(user);
        } else {
            post.getSavedBy().add(user);
        }
        return modelMapper.map(postRepository.save(post), PostResponse.class);
    }

    @Override
    public GlobalApiResponse<?> getAllSavedPostByUserId(Long userId, int page, int size) throws NoSuchFieldException {

        Pageable pageable = PageRequest.of(page, size);

        Optional<UserModel> userModelOptional= userRepo.findById(userId);
        if(userModelOptional.isEmpty()) throw new NoSuchFieldException("User not present with this id: {}"+ userId);
        Page<PostModel> postModelPage = postRepository.findBySavedBy(userModelOptional.get(), pageable);

        // Convert List<PostModel> to List<PostResponse>
        List<PostResponse> postResponses = postModelPage.getContent()
                .stream()
                .map(p -> modelMapper.map(p, PostResponse.class))
                .toList();

        PageableResponse<List<PostResponse>> pageableResponse = new PageableResponse<>(
                postResponses,
                postModelPage.getTotalPages(),
                postModelPage.getNumber(),
                postModelPage.getSize(),
                postModelPage.getTotalElements(),
                postModelPage.isLast()
        );

        return new GlobalApiResponse<>(
                pageableResponse,
                "Saved Posts retrieved successfully",
                true
        );

    }
}
