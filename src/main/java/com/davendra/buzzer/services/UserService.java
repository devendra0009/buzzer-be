package com.davendra.buzzer.services;

import com.davendra.buzzer.dto.request.LoginRequest;
import com.davendra.buzzer.dto.request.RegisterRequest;
import com.davendra.buzzer.dto.response.AuthResponse;
import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.entity.UserModel;

import java.io.IOException;
import java.util.List;

public interface UserService {
    public AuthResponse registerUser(RegisterRequest registerRequest) throws IOException;

    public AuthResponse loginUser(LoginRequest user);
    public AuthResponse loginUserWithGoogle(LoginRequest user);

    public GlobalApiResponse<?> searchUser(String query, int page, int size);

    public GlobalApiResponse<?> getAllUsers(String sortBy, int page, int size);

    public UserModel findUserById(Long userId);

    public List<UserModel> findAllUsersById(List<Long> userIds);

    public UserModel findUserByEmail(String email);

    public GlobalApiResponse<?> findUserByUserName(String username);

    public UserModel followUser(Long userId1, Long userId2) throws Exception;

    public UserModel updateUserDetails(UserModel user, Long id);

    public UserModel getUserFromToken(String token);
}
