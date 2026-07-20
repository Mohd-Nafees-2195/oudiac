package com.app.oudiac.services.emailOtpService;

import com.app.oudiac.dtos.emailDto.OtpData;
import com.app.oudiac.exceptions.UserNotFoundException;
import com.app.oudiac.models.Admin;
import com.app.oudiac.models.User;
import com.app.oudiac.models.enums.EmailStatus;
import com.app.oudiac.repositories.AdminRepository;
import com.app.oudiac.repositories.UserRepository;
import com.app.oudiac.services.JWTService.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final Map<String, OtpData> cacheStore = new ConcurrentHashMap<>();

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final JwtService jwtService;


    public void sendOtp(String email) {

//        if (!userRepository.existsByEmail(email)) {
//            throw new UserNotFoundException("Please register first!!");
//        }

        String otp = generateOtp();

        long expiry = System.currentTimeMillis() + 5 * 60 * 1000; // 5 min

        cacheStore.put(email, new OtpData(otp, expiry));

        emailService.sendOtp(email, otp);
    }

    public ResponseEntity<String> verifyUserOtp(String email, String otp) {

        OtpData data = cacheStore.get(email);

        //Uncomment below code , commented for by pass
//        if (data == null) {
//            throw new RuntimeException("OTP not found");
//        }
//
//        if (System.currentTimeMillis() > data.getExpiry()) {
//            cacheStore.remove(email);
//            throw new RuntimeException("OTP expired");
//        }
//
//        if (!data.getOtp().equals(otp)) {
//            throw new RuntimeException("Invalid OTP");
//        }

        Optional<User> user=userRepository.findByEmail(email);
        if(user.get().getEmailStatus()==EmailStatus.NOT_VERIFIED){
            user.get().setEmailStatus(EmailStatus.VERIFIED);
            userRepository.save(user.get());
        }

        cacheStore.remove(email);

        //Generate the JWT token
        String token=jwtService.generateJwtTokenForUser(user.get());

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(false) // true if only backend should read it
                .secure(false)   // true in HTTPS
                .path("/")
                .maxAge(Duration.ofDays(10))
                .sameSite("Lax")
                .build();

        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE,cookie.toString());
        System.out.println(token);
        return new ResponseEntity<>("Login Success "+email,headers , HttpStatus.OK);
    }

    public ResponseEntity<String> verifyAdminOtp(String email, String otp) {

        OtpData data = cacheStore.get(email);

        if (data == null) {
            throw new RuntimeException("OTP not found");
        }

        if (System.currentTimeMillis() > data.getExpiry()) {
            cacheStore.remove(email);
            throw new RuntimeException("OTP expired");
        }

        if (!data.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        Optional<Admin> admin=adminRepository.findByEmail(email);
        admin.get().setEmailStatus(EmailStatus.VERIFIED);
        adminRepository.save(admin.get());

        cacheStore.remove(email);

        //Generate the JWT token
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
        return new ResponseEntity<>("Login Success "+email,headers , HttpStatus.OK);
    }

    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

}