package com.app.oudiac.dtos.userDtos;

import com.app.oudiac.models.Admin;
import com.app.oudiac.models.User;
import com.app.oudiac.models.enums.EmailStatus;
import com.app.oudiac.models.enums.Role;
import lombok.Data;

import java.util.Date;

@Data
public class UserRegisterResponseDto {
    private Long id;
    private String name;
    private String email;
    private String mobileNumber;
    private Date created_at;
    private Date updated_at;

    private Long totalOrderOfStore;
    private EmailStatus emailStatus;
    private Role role;

    private String message;

    public static UserRegisterResponseDto convertFromUser(User user){
        UserRegisterResponseDto response=new UserRegisterResponseDto();

        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setMobileNumber(user.getMobileNumber());
        response.setEmailStatus(user.getEmailStatus());

        response.setCreated_at(user.getCreated_at());
        response.setUpdated_at(user.getUpdated_at());
        response.setRole(user.getRole());
        return  response;
    }
    public static UserRegisterResponseDto convertFromAdmin(Admin user){
        UserRegisterResponseDto response=new UserRegisterResponseDto();

        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setMobileNumber(user.getMobileNumber());
        response.setEmailStatus(user.getEmailStatus());

        response.setCreated_at(user.getCreated_at());
        response.setUpdated_at(user.getUpdated_at());
        response.setRole(user.getRole());
        return  response;
    }
}
