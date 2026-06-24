package com.app.oudiac.services.emailOtpService;

import com.app.oudiac.dtos.userDtos.AdminUserLoginRequestDto;
import com.app.oudiac.exceptions.UserNotFoundException;
import com.app.oudiac.models.Admin;
import com.app.oudiac.models.enums.EmailStatus;
import com.app.oudiac.repositories.AdminRepository;
import com.app.oudiac.services.JWTService.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OtpService otpService;
    private final JwtService jwtService;
    private final AdminRepository adminRepository;


    public ResponseEntity<String> login(@Valid AdminUserLoginRequestDto request) {

        System.out.println(request.getEmail()+" - "+request.getPassword());
        Optional<Admin> admin=adminRepository.findByEmail(request.getEmail());
        if(admin.isEmpty()){
            throw new UserNotFoundException("Invalid user");
        }
        if(admin.get().getEmailStatus()== EmailStatus.NOT_VERIFIED){
            otpService.sendOtp(request.getEmail());
        }else{
            String token=jwtService.generateJwtTokenForAdmin(admin.get());

            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(false) // true if only backend should read it
                    .secure(false)   // true in HTTPS
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .sameSite("Lax")
                    .build();

            MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.SET_COOKIE,cookie.toString());
            System.out.println(token);
            return new ResponseEntity<>("Login Success "+request.getEmail(),headers , HttpStatus.OK);
        }
        return new ResponseEntity<>("OTP has been sent to "+request.getEmail(), HttpStatus.OK);

    }
}
