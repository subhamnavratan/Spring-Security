package com.Subham.PRASAG.dto;

public class VerifyForgotPasswordDto {
    private String email;
    private Integer otp;
    private ResetPassword resetPassword;

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

    public ResetPassword getResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(ResetPassword resetPassword) {
        this.resetPassword = resetPassword;
    }
}
