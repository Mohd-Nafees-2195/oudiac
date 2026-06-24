package com.app.oudiac.dtos.userDtos;

import com.app.oudiac.models.Admin;
import com.app.oudiac.models.User;
import com.app.oudiac.models.enums.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

@Data
public class UserRegisterRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian mobile number")
    private String mobileNumber;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters, include uppercase, lowercase, number and special character"
    )
    private String password;

    public static User fromUserRegisterRequestDtoToUser(UserRegisterRequestDto requestDto){
        User user=new User();
        user.setEmail(requestDto.getEmail());
        user.setName(requestDto.getName());
        user.setMobileNumber(requestDto.getMobileNumber());
        user.setCreated_at(new Date());
        user.setUpdated_at(new Date());
        user.setIsDeleted(false);
        return user;
    }

    public static Admin fromUserRegisterRequestDtoToAdmin(UserRegisterRequestDto requestDto){
        Admin user=new Admin();
        user.setEmail(requestDto.getEmail());
        user.setName(requestDto.getName());
        user.setMobileNumber(requestDto.getMobileNumber());
        user.setCreated_at(new Date());
        user.setUpdated_at(new Date());
        user.setIsDeleted(false);
        return user;
    }

}
