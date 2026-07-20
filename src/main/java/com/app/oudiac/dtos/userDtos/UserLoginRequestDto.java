package com.app.oudiac.dtos.userDtos;

import com.app.oudiac.models.User;
import com.app.oudiac.models.enums.EmailStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    public static User fromUserUserLoginRequestDtoUser(@Valid UserLoginRequestDto request) {
        User newUser=new User();
        newUser.setEmail(request.getEmail());
        newUser.setEmailStatus(EmailStatus.NOT_VERIFIED);
        return newUser;
    }
}
