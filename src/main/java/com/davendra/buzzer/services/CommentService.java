package com.davendra.buzzer.services;

import com.davendra.buzzer.dto.request.CommentRequest;
import com.davendra.buzzer.dto.response.CommentResponse;
import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.entity.CommentModel;

import java.util.List;

public interface CommentService {
    public GlobalApiResponse<?> getCommentsByUserId(Long userId, int page, int size);

    public GlobalApiResponse<?> getCommentsByPostId(Long postId, int page, int size);

    public CommentModel getCommentsById(Long commentId);

    public CommentResponse createComment(CommentRequest commentReq, Long commentedByUserId);

    public CommentResponse likeComment(Long commentId, Long likedByUserId);

    public String deleteComment(Long commentId, Long commentedByUserId) throws Exception; // only delete your own comments
}
