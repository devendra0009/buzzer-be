package com.davendra.buzzer.serviceImpl;

import com.davendra.buzzer.config.JwtProvider;
import com.davendra.buzzer.dto.request.LoginRequest;
import com.davendra.buzzer.dto.request.RegisterRequest;
import com.davendra.buzzer.dto.response.*;
import com.davendra.buzzer.entity.UserModel;
import com.davendra.buzzer.repositories.UserRepo;
import com.davendra.buzzer.services.CloudinaryUploadService;
import com.davendra.buzzer.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.davendra.buzzer.config.JwtProvider.generateToken;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;
    @Autowired
    private ModelMapper modelMapper;

    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (userDetails == null) {
            throw new BadCredentialsException("Wrong credentials");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Wrong credentials");

        }
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
    }

    @Override
    public AuthResponse loginUser(LoginRequest user) {

        Authentication authentication = authenticate(user.getEmail(), user.getPassword());

        String token = generateToken(authentication);

        return AuthResponse.builder().token(token).message("Login successfully").build();

    }

    @Override
    public AuthResponse loginUserWithGoogle(LoginRequest user) {

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user.getEmail(), null, Collections.emptyList());


        String token = generateToken(authentication);

        return AuthResponse.builder().token(token).message("Login successfully with google").build();

    }

    @Override
    public AuthResponse registerUser(RegisterRequest registerRequest) throws IOException {
        if (registerRequest.getEmail() != null && !registerRequest.getEmail().isEmpty()) {
            if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email is already registered.");
            }
        }
        if (registerRequest.getPhone() != null && !registerRequest.getPhone().isEmpty()) {
            if (userRepository.findByPhone(registerRequest.getPhone()).isPresent()) {
                throw new IllegalArgumentException("Phone is already registered.");
            }
        }


        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        UserModel userModel = modelMapper.map(registerRequest, UserModel.class);

        // here error may occur

        userModel.setProfileImg(registerRequest.getProfileImg());
        UserModel savedUser = userRepository.save(userModel);

        // now generate a jwt
        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());

        String token = generateToken(authentication);

        return AuthResponse.builder().token(token).message("Registered successfully").build();
    }

    public GlobalApiResponse<?> searchUser(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserModel> userPage = userRepository.searchByKeyword(query, pageable);
        List<UserResponse> userResponseList = userPage.getContent()
                .stream()
                .map(usr -> modelMapper.map(usr, UserResponse.class))
                .toList();

        PageableResponse<List<UserResponse>> pageableResponse = new PageableResponse<>(
                userResponseList,
                userPage.getTotalPages(),
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.isLast()
        );

        return new GlobalApiResponse<>(pageableResponse, "Searched User's retrieved successfully", true);
    }


    @Override
    public GlobalApiResponse<?> getAllUsers(String sortBy, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserModel> userPage = userRepository.findUsersSorted(sortBy, pageable);
        List<UserResponse> userResponseList = userPage.getContent()
                .stream()
                .map(usr -> modelMapper.map(usr, UserResponse.class))
                .toList();

        PageableResponse<List<UserResponse>> pageableResponse = new PageableResponse<>(
                userResponseList,
                userPage.getTotalPages(),
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.isLast()
        );

        return new GlobalApiResponse<>(pageableResponse, "All User's retrieved successfully", true);
    }

    @Override
    public UserModel findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
    }

    @Override
    public List<UserModel> findAllUsersById(List<Long> userIds) {
        return userRepository.findAllById(userIds);
    }

    @Override
    public UserModel findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found with email: " + email));
    }

    @Override
    public GlobalApiResponse<?> findUserByUserName(String username) {

        AtomicBoolean exists = new AtomicBoolean(false);
        userRepository.findByUserName(username).ifPresent(userModel -> {
            exists.set(true);
        });

        if (exists.get()) {
            return new GlobalApiResponse<>(null, "Username already exists", false);
        }

        return new GlobalApiResponse<>(null, "Username available", true);
//        return userRepository.findByUserName(username)
//                .orElseThrow(() -> new NoSuchElementException("User not found with email: " + username));
    }

    @Override
    public UserModel followUser(Long userId1, Long userId2) throws Exception {
        if (Objects.equals(userId1, userId2)) {
            throw new Exception("Can't follow yourself");
        }

        // user1 wants to follow/unfollow user2
        UserModel user1 = findUserById(userId1);
        UserModel user2 = findUserById(userId2);

        // Toggle follow/unfollow
        if (user1.getFollowings().contains(user2.getId())) {
            // If already following, remove
            user1.getFollowings().remove(user2.getId());
            user2.getFollowers().remove(user1.getId());
        } else {
            // If not following, add
            user1.getFollowings().add(user2.getId());
            user2.getFollowers().add(user1.getId());
        }

        // Save changes to the database
        userRepository.saveAll(Arrays.asList(user1, user2));

        return user1;
    }


    @Override
    public UserModel updateUserDetails(UserModel user, Long id) {
        UserModel existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + user.getId()));

        if (user.getFirstName() != null) {
            existingUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            existingUser.setLastName(user.getLastName());
        }
        if (user.getEmail() != null) {
            if (userRepository.findByEmail(user.getEmail()).isPresent() &&
                    !user.getEmail().equals(existingUser.getEmail())) {
                throw new IllegalArgumentException("Email is already registered to another user.");
            }
            existingUser.setEmail(user.getEmail());
        }
        if (user.getPhone() != null) {
            existingUser.setPhone(user.getPhone());
        }
        if (user.getGender() != null) {
            existingUser.setGender(user.getGender());
        }

        return userRepository.save(existingUser);
    }

    public UserModel getUserFromToken(String token) {
        String email = JwtProvider.getEmailFromJwt(token);
        return findUserByEmail(email);
    }
}
