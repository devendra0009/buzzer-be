package com.davendra.buzzer.controller;

import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.entity.UserModel;
import com.davendra.buzzer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

//    @PostMapping("/register")
//    public ResponseEntity<UserModel> registerUser(@RequestBody UserModel user) {
//        UserModel registeredUser = userService.registerUser(user);
//        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
//    }

    @GetMapping("/search")
    public ResponseEntity<GlobalApiResponse<?>> searchUser(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(userService.searchUser(query, page, size));
    }

    @GetMapping
    public ResponseEntity<GlobalApiResponse<?>> getAllUsers(@RequestParam(defaultValue = "") String sortBy,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(userService.getAllUsers(sortBy, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Long id) {
        UserModel user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email")
    public ResponseEntity<UserModel> getUserByEmail(@RequestParam String email) {
        UserModel user = userService.findUserByEmail(email);
        return ResponseEntity.ok(user);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserModel> updateUserDetails(@RequestHeader("Authorization") String token, @RequestBody UserModel userReq) {
        UserModel user = userService.updateUserDetails(userReq, userService.getUserFromToken(token).getId());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/follow/{user2}")
    public ResponseEntity<UserModel> followUser(@RequestHeader("Authorization") String token, @PathVariable Long user2) throws Exception {
        UserModel user = userService.followUser(userService.getUserFromToken(token).getId(), user2);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserModel> getUserFromToken(@RequestHeader("Authorization") String token) {
        UserModel user = userService.getUserFromToken(token);
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }
}
