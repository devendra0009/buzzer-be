package com.davendra.buzzer.controller;

import com.davendra.buzzer.dto.request.LoginRequest;
import com.davendra.buzzer.dto.request.RegisterRequest;
import com.davendra.buzzer.dto.response.AuthResponse;
import com.davendra.buzzer.dto.response.GlobalApiResponse;
import com.davendra.buzzer.entity.UserModel;
import com.davendra.buzzer.enums.GenderEnum;
import com.davendra.buzzer.repositories.UserRepo;
import com.davendra.buzzer.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth/google")
@Slf4j
public class GoogleAuthController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;


    @PostMapping("/callback")
    public ResponseEntity<AuthResponse> handleGoogleCallback(@RequestParam String code) throws Exception {

        // Exchange auth code with tokens (fe send auth code to backend)
        String tokenEndpoint = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
//        body.add("redirect_uri", "https://developers.google.com/oauthplayground");
        body.add("redirect_uri", "http://localhost:3000");
        body.add("grant_type", "authorization_code");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, requestEntity, Map.class);

        String idToken = (String) tokenResponse.getBody().get("id_token");
        String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
        ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);
        try {


            if (userInfoResponse.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> userInfo = userInfoResponse.getBody();
                String email = (String) userInfo.get("email");
                String firstName = (String) userInfo.get("given_name");
                String lastName = (String) userInfo.get("family_name");
                String profileImg = (String) userInfo.get("picture");
                String genderStr = (String) userInfo.get("gender"); // Google returns "male" or "female"
                UserModel userModel = null;
                try {
                    userModel = userService.findUserByEmail(email);
                    if (userModel != null) {
                        LoginRequest loginRequest = LoginRequest.builder().email(email).build();
                        AuthResponse authResponse = userService.loginUserWithGoogle(loginRequest);
                        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
                    }
                } catch (Exception e) {
                    if (userModel == null) {
                        String randomSuffix = String.valueOf((int) (Math.random() * 9000) + 1000); // Random 4-digit number
                        String userName = firstName + lastName + randomSuffix;

                        GenderEnum gender = null;
                        if (genderStr != null) {
                            try {
                                gender = GenderEnum.valueOf(genderStr.toUpperCase()); // Convert "male" -> MALE
                            } catch (IllegalArgumentException ex) {
                                ex.printStackTrace();
                                log.info(ex.getMessage());
                            }
                        }
                        RegisterRequest registerRequest = RegisterRequest.builder().firstName(firstName).lastName(lastName).email(email).userName(userName).profileImg(profileImg).phone("").gender(gender).password(UUID.randomUUID().toString()).build();
                        AuthResponse registeredUserWithGoogle = userService.registerUser(registerRequest);
                        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUserWithGoogle);
                    }
                }
            } else {
                throw new Exception("Some error occured while loggin in with google");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception ey) {
            throw ey;
        }
    }

}
