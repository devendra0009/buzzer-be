package com.davendra.buzzer.dto.response;

import com.davendra.buzzer.enums.GenderEnum;
import jakarta.persistence.ElementCollection;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String phone;
    private String profileImg;
    private List<Long> followers;
    private List<Long> followings;
    private GenderEnum gender;
}
