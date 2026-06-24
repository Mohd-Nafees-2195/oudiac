package com.app.oudiac.services.userService;

import com.app.oudiac.dtos.userDtos.UserInfoDto;
import com.app.oudiac.dtos.userDtos.UserLoginRequestDto;
import com.app.oudiac.dtos.userDtos.UserRegisterRequestDto;
import com.app.oudiac.dtos.userDtos.UserRegisterResponseDto;
import com.app.oudiac.exceptions.UserAlreadyExitException;
import com.app.oudiac.exceptions.UserNotFoundException;
import com.app.oudiac.models.User;
import com.app.oudiac.models.enums.EmailStatus;
import com.app.oudiac.models.enums.Role;
import com.app.oudiac.repositories.UserRepository;
import com.app.oudiac.services.JWTService.JwtService;
import com.app.oudiac.services.emailOtpService.OtpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private JwtService jwtService;

    public ResponseEntity<String> login(UserLoginRequestDto request) {

        Optional<User> userOptional=userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
             User newUser=UserLoginRequestDto.fromUserUserLoginRequestDtoUser(request);
             newUser.setRole(Role.USER);
             //Save to db
             userRepository.save(newUser);
        }
        //Send OTP
        if(userOptional.get().getEmailStatus()== EmailStatus.NOT_VERIFIED){
            otpService.sendOtp(request.getEmail());
        }else {
            //Create JWT Token only
            String token=jwtService.generateJwtToken(userOptional.get());
            MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.SET_COOKIE,"jwt="+token);
            return new ResponseEntity<>("OTP has been sent to "+request.getEmail(),headers ,HttpStatus.OK);
        }
        return new ResponseEntity<>("OTP has been sent to "+request.getEmail(), HttpStatus.OK);
    }


    //Get User By Id
    public ResponseEntity<UserInfoDto> getUserById(Long id) {

        User user= userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserInfoDto response=UserInfoDto.fromUserToUserInfoDto(user);

        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
