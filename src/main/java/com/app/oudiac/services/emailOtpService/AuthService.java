package com.app.oudiac.services.emailOtpService;

import com.app.oudiac.dtos.emailDto.EmailOTPRequestDto;
import com.app.oudiac.dtos.userDtos.AdminUserLoginRequestDto;
import com.app.oudiac.exceptions.InvalidCredentialsException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder bCryptPasswordEncoder;


    public ResponseEntity<String> adminLogin(@Valid AdminUserLoginRequestDto request) {

        System.out.println(request.getEmail()+" - "+request.getPassword());
        Optional<Admin> admin=adminRepository.findByEmail(request.getEmail());
        if(admin.isEmpty()){
            throw new UserNotFoundException("Invalid user");
        }
        if (!bCryptPasswordEncoder.matches(request.getPassword(), admin.get().getPassword())){
            throw new InvalidCredentialsException("Invalid password");
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

    public void sendOtp(@Valid EmailOTPRequestDto request) {
        otpService.sendOtp(request.getEmail());
    }

    public ResponseEntity<String> verifyAdminOtp(String email, String otp) {
       return otpService.verifyAdminOtp(email, otp);
    }
    public ResponseEntity<String> verifyUserOtp(String email, String otp) {
        return otpService.verifyUserOtp(email, otp);
    }
}
