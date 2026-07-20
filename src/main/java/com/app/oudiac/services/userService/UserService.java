package com.app.oudiac.services.userService;

import com.app.oudiac.dtos.addressDtos.AddressRequestDto;
import com.app.oudiac.dtos.addressDtos.AddressResponseDto;
import com.app.oudiac.dtos.userDtos.UserInfoDto;
import com.app.oudiac.dtos.userDtos.UserLoginRequestDto;
import com.app.oudiac.dtos.userDtos.UserRegisterRequestDto;
import com.app.oudiac.dtos.userDtos.UserRegisterResponseDto;
import com.app.oudiac.exceptions.UserAlreadyExitException;
import com.app.oudiac.exceptions.UserNotFoundException;
import com.app.oudiac.models.Address;
import com.app.oudiac.models.User;
import com.app.oudiac.models.enums.EmailStatus;
import com.app.oudiac.models.enums.Role;
import com.app.oudiac.repositories.AddressRepository;
import com.app.oudiac.repositories.UserRepository;
import com.app.oudiac.services.JWTService.JwtService;
import com.app.oudiac.services.emailOtpService.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final OtpService otpService;

    private final JwtService jwtService;

    public ResponseEntity<String> login(UserLoginRequestDto request) {

        Optional<User> userOptional=userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
             User newUser=UserLoginRequestDto.fromUserUserLoginRequestDtoUser(request);
             newUser.setRole(Role.USER);
             //Save to db
             userRepository.save(newUser);
        }
        //Send OTP  Uncomment below code after coding, commneted toby pass otp
//        otpService.sendOtp(request.getEmail());
        return new ResponseEntity<>("OTP has been sent to "+request.getEmail(), HttpStatus.OK);
    }


    //Get User By Id
    public ResponseEntity<UserInfoDto> getUserById(Long id) {

        User user= userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        UserInfoDto response=UserInfoDto.fromUserToUserInfoDto(user);

        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public ResponseEntity<AddressResponseDto> addAddress(@Valid AddressRequestDto request,String email) {
        Address newAddress=AddressRequestDto.fromRequestDto(request);
        Optional<User> userOptional=userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        newAddress.setUser(userOptional.get());
        addressRepository.save(newAddress);
//        System.out.println(email+"-"+newAddress.getShippingAddress() +" | "+ newAddress.getCountry());
        AddressResponseDto response=AddressResponseDto.fromAddressEntity(newAddress);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    public ResponseEntity<List<AddressResponseDto>> getAddress(String email) {
        Optional<User> userOptional=userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        List<Address> addressList=addressRepository.findAllByUserId(userOptional.get().getId());
        List<AddressResponseDto> responseList=new ArrayList<>();
        for(Address address:addressList){
            AddressResponseDto response=AddressResponseDto.fromAddressEntity(address);
            responseList.add(response);
        }
        return new ResponseEntity<>(responseList,HttpStatus.OK);
    }
}
