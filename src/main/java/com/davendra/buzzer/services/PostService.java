package com.davendra.buzzer.services;

import com.davendra.buzzer.dto.request.PostRequest;
import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.dto.response.PageableResponse;
import com.davendra.buzzer.dto.response.PostResponse;
import com.davendra.buzzer.entity.PostModel;

import java.io.IOException;
import java.util.List;

public interface PostService {
    public PostResponse createPost(PostRequest post, Long userId) throws IOException;

    public String deletePost(Long postId, Long userId) throws Exception;

    public GlobalApiResponse<?> findPostsByUserId(Long userId, int page, int size);

    public PostModel findPostsById(Long postId);

    public GlobalApiResponse<PageableResponse<List<PostResponse>>> findAllPost(int page, int size);

    public PostResponse likePost(Long userId, Long postId);

    public PostResponse savePost(Long userId, Long postId);

    public GlobalApiResponse<?> getAllSavedPostByUserId(Long userId, int page, int size) throws NoSuchFieldException;

//    public PostModel updatePost(PostModel post, Long postId);
}
