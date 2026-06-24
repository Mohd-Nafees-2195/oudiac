package com.app.oudiac.services.emailOtpService;

import com.app.oudiac.dtos.emailDto.OtpData;
import com.app.oudiac.exceptions.UserNotFoundException;
import com.app.oudiac.models.User;
import com.app.oudiac.models.enums.EmailStatus;
import com.app.oudiac.repositories.UserRepository;
import com.app.oudiac.services.JWTService.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final Map<String, OtpData> cacheStore = new ConcurrentHashMap<>();

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;


    public void sendOtp(String email) {

//        if (!userRepository.existsByEmail(email)) {
//            throw new UserNotFoundException("Please register first!!");
//        }

        String otp = generateOtp();

        long expiry = System.currentTimeMillis() + 5 * 60 * 1000; // 5 min

        cacheStore.put(email, new OtpData(otp, expiry));

        emailService.sendOtp(email, otp);
    }

    public ResponseEntity<String> verifyOtp(String email, String otp) {

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

        Optional<User> user=userRepository.findByEmail(email);
        user.get().setEmailStatus(EmailStatus.VERIFIED);
        userRepository.save(user.get());

        cacheStore.remove(email);

        //Generate the JWT token
        String token=jwtService.generateJwtToken(user.get());

        //Set Headers
        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE,token);
        return new ResponseEntity<>("Login Success",headers,HttpStatus.OK);
    }

    private String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

}