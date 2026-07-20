package com.app.oudiac.dtos.emailDto;

import com.app.oudiac.models.enums.Role;
import lombok.Data;

@Data
public class OtpData {
    private String otp;
    private Long expiry;
    private Role role;

    public OtpData(String otp, long expiry) {
        this.otp=otp;
        this.expiry=expiry;
    }
}
