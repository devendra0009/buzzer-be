package com.davendra.buzzer.controller;

import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.dto.response.PageableResponse;
import com.davendra.buzzer.dto.response.PostResponse;
import com.davendra.buzzer.repositories.PostRepo;
import com.davendra.buzzer.serviceImpl.PostServiceImpl;
import com.davendra.buzzer.serviceImpl.UserServiceImpl;
import com.davendra.buzzer.services.PostService;
import com.davendra.buzzer.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class PostControllerTest {

    @InjectMocks // use for class under test
    private PostController postController;
    @Mock
    private PostService postService;
    //    @InjectMocks
//    private UserService userService;
//    @InjectMocks
//    private PostServiceImpl postServiceImpl;
    @Mock
    private UserServiceImpl userServiceImpl;
    @Mock
    private PostRepo postRepo;

    @Test
    public void findAllPosts() {

        // Arrange
        GlobalApiResponse<PageableResponse<List<PostResponse>>> globalApiResponse = new GlobalApiResponse<>();
        int page = 0;
        int sz = 10;
        log.info("find all post");

        // Act
//        doReturn(globalApiResponse).when(postService).findAllPost(page, sz);
        when(postService.findAllPost(page, sz)).thenReturn(globalApiResponse); // in this it doesn't accept generic type ? means ? -> capture# ? not assignable

        ResponseEntity<GlobalApiResponse<?>> response = postController.findAllPosts(page, sz);
        log.info("response: {}", response);

        // Verify -> so i want it that response should not be null
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNull(response.getBody().getData());
    }
}
