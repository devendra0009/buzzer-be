package com.davendra.buzzer.serviceImpl;

import com.davendra.buzzer.dto.request.CommentRequest;
import com.davendra.buzzer.dto.response.CommentResponse;
import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.dto.response.PageableResponse;
import com.davendra.buzzer.dto.response.StoryResponse;
import com.davendra.buzzer.entity.CommentModel;
import com.davendra.buzzer.entity.PostModel;
import com.davendra.buzzer.entity.UserModel;
import com.davendra.buzzer.repositories.CommentRepo;
import com.davendra.buzzer.repositories.PostRepo;
import com.davendra.buzzer.services.CommentService;
import com.davendra.buzzer.services.PostService;
import com.davendra.buzzer.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public GlobalApiResponse<?> getCommentsByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentModel> commentModelPage = commentRepo.findByCommentedById(userId, pageable);
        List<CommentResponse> commentResponseList = commentModelPage.getContent()
                .stream()
                .map(story -> modelMapper.map(story, CommentResponse.class))
                .toList();

        PageableResponse<List<CommentResponse>> pageableResponse = new PageableResponse<>(
                commentResponseList,
                commentModelPage.getTotalPages(),
                commentModelPage.getNumber(),
                commentModelPage.getSize(),
                commentModelPage.getTotalElements(),
                commentModelPage.isLast()
        );

        return new GlobalApiResponse<>(pageableResponse, "User's comments retrieved successfully", true);
    }

    @Override
    public GlobalApiResponse<?> getCommentsByPostId(Long postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentModel> commentModelPage = commentRepo.findByPostId(postId, pageable);
        List<CommentResponse> commentResponseList = commentModelPage.getContent()
                .stream()
                .map(story -> modelMapper.map(story, CommentResponse.class))
                .toList();

        PageableResponse<List<CommentResponse>> pageableResponse = new PageableResponse<>(
                commentResponseList,
                commentModelPage.getTotalPages(),
                commentModelPage.getNumber(),
                commentModelPage.getSize(),
                commentModelPage.getTotalElements(),
                commentModelPage.isLast()
        );

        return new GlobalApiResponse<>(pageableResponse, "Post's comments retrieved successfully", true);
    }

    @Override
    public CommentModel getCommentsById(Long commentId) {
        return commentRepo.findById(commentId).orElseThrow(() -> new NoSuchElementException("Comment not found!"));
    }

    @Override
    public CommentResponse createComment(CommentRequest commentReq, Long commentedByUserId) {
        PostModel existingPost = postService.findPostsById(commentReq.getPostId());
        UserModel existingUser = userService.findUserById(commentedByUserId);
        CommentModel commentModel = new CommentModel();

        // Map properties except ID
        modelMapper.typeMap(CommentRequest.class, CommentModel.class)
                .addMappings(mapper -> mapper.skip(CommentModel::setId));
        modelMapper.map(commentReq, commentModel);

        commentModel.setCommentedBy(existingUser);
        commentModel.setPost(existingPost);
        CommentModel commentModel1 = commentRepo.save(commentModel);
        return modelMapper.map(commentModel1, CommentResponse.class);
    }

    @Override
    public CommentResponse likeComment(Long commentId, Long likedByUserId) {
        CommentModel existingComment = getCommentsById(commentId);
        UserModel existingUser = userService.findUserById(likedByUserId);

        if (!existingComment.getLikedBy().contains(existingUser)) {
            existingComment.getLikedBy().add(existingUser);
        } else {
            existingComment.getLikedBy().remove(existingUser);
        }
        return modelMapper.map(commentRepo.save(existingComment), CommentResponse.class);
    }

    @Override
    public String deleteComment(Long commentId, Long commentedByUserId) throws Exception {
        CommentModel commentModel = getCommentsById(commentId);
        if (!Objects.equals(commentModel.getCommentedBy().getId(), commentedByUserId)) {
            throw new Exception("Can't delete other's comment");
        }
        commentRepo.deleteById(commentId);
        return "Comment delete";
    }
}
