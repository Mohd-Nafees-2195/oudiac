package com.app.oudiac.controllers;

import com.app.oudiac.dtos.userDtos.AdminUserLoginRequestDto;
import com.app.oudiac.dtos.userDtos.UserRegisterRequestDto;
import com.app.oudiac.dtos.userDtos.UserRegisterResponseDto;
import com.app.oudiac.services.adminService.AdminService;
import com.app.oudiac.services.userService.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Create Admin
    @PostMapping("/oudiac/register-admin")
    public ResponseEntity<UserRegisterResponseDto> createAdmin(@Valid @RequestBody UserRegisterRequestDto request) {
        return adminService.registerAdmin(request);
    }
    @PostMapping("/oudiac/register-manager")
    public ResponseEntity<UserRegisterResponseDto> createManager(@Valid @RequestBody UserRegisterRequestDto request) {
        return adminService.registerManager(request);
    }

    @GetMapping("/oudiac/get-managers")
    public ResponseEntity<List<UserRegisterResponseDto>> getManager() {
        return adminService.getManagers();
    }

}
