package com.Subham.PRASAG.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class VerifySignupDto {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank
    private Integer otp;
    private Signupdto signupdto;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public Signupdto getSignupdto() {
        return signupdto;
    }

    public void setSignupdto(Signupdto signupdto) {
        this.signupdto = signupdto;
    }
}
