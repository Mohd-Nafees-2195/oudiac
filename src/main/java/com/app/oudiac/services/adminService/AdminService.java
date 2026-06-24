package com.app.oudiac.services.adminService;

import com.app.oudiac.dtos.userDtos.AdminUserLoginRequestDto;
import com.app.oudiac.dtos.userDtos.UserRegisterRequestDto;
import com.app.oudiac.dtos.userDtos.UserRegisterResponseDto;
import com.app.oudiac.exceptions.UserAlreadyExitException;
import com.app.oudiac.exceptions.UserNotFoundException;
import com.app.oudiac.models.Admin;
import com.app.oudiac.models.User;
import com.app.oudiac.models.enums.EmailStatus;
import com.app.oudiac.models.enums.Role;
import com.app.oudiac.repositories.AdminRepository;
import com.app.oudiac.repositories.UserRepository;
import com.app.oudiac.services.JWTService.JwtService;
import com.app.oudiac.services.emailOtpService.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    private final OtpService otpService;
    private final JwtService jwtService;

    private final PasswordEncoder bCryptPasswordEncoder;

    //Register Admin
    public ResponseEntity<UserRegisterResponseDto> registerAdmin(UserRegisterRequestDto user) {
        //Validation done
        // Check if email already exists
        if (adminRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExitException("Email already in use");
        }

        Admin newUser=UserRegisterRequestDto.fromUserRegisterRequestDtoToAdmin(user);
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        newUser.setRole(Role.ADMIN);
        adminRepository.save(newUser);

        otpService.sendOtp(user.getEmail());

        UserRegisterResponseDto responseDto= UserRegisterResponseDto.convertFromAdmin(newUser);
        responseDto.setMessage("Registration Successful");
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<UserRegisterResponseDto> registerManager(UserRegisterRequestDto user) {
        //Validation done
        // Check if email already exists
        if (adminRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExitException("Email already in use");
        }

        Admin newUser=UserRegisterRequestDto.fromUserRegisterRequestDtoToAdmin(user);
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        newUser.setRole(Role.MANAGER);

        adminRepository.save(newUser);

        otpService.sendOtp(user.getEmail());

        UserRegisterResponseDto responseDto= UserRegisterResponseDto.convertFromAdmin(newUser);
        responseDto.setMessage("Registration Successful");
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    public ResponseEntity<List<UserRegisterResponseDto>> getManagers() {
        List<Admin> users=adminRepository.findAllByRole(Role.MANAGER);
        List<UserRegisterResponseDto> response=new ArrayList<>();
        for(Admin user:users){
            response.add(UserRegisterResponseDto.convertFromAdmin(user));
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
