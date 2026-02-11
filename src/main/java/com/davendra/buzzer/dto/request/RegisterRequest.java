package com.davendra.buzzer.dto.request;

import com.davendra.buzzer.enums.GenderEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String phone;
    private String password;
    private GenderEnum gender;
    private String profileImg;
}
