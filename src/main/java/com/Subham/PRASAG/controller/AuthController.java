package com.Subham.PRASAG.controller;

import com.Subham.PRASAG.dto.*;
import com.Subham.PRASAG.service.AuthService;
import com.Subham.PRASAG.service.CustomUserDetailsService;
import com.Subham.PRASAG.service.JwtService;
import com.Subham.PRASAG.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(AuthService authService,
                          RefreshTokenService refreshTokenService,
                          JwtService jwtService,
                          CustomUserDetailsService userDetailsService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    // ===================== SIGNUP =====================

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Signupdto signupdto) {
        authService.signupSendOtp(signupdto);
        return ResponseEntity.ok("OTP sent to email");
    }
    @PostMapping("/verify-signup")
    public ResponseEntity<String> verifySignup(
            @RequestBody VerifySignupDto dto) {

        authService.verifySignupOtp(
                dto.getEmail(),
                dto.getOtp(),
                dto.getSignupdto()
        );

        return ResponseEntity.ok("Signup successful");
    }

    // ===================== LOGIN =====================

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody Logindto logindto) {

        return ResponseEntity.ok(authService.login(logindto));
    }

    // ===================== REFRESH TOKEN =====================

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody RefreshTokenRequest request) {

        return ResponseEntity.ok(authService.refreshAccessToken(request));
    }

    // ===================== FORGOT PASSWORD =====================

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestParam String email) {

        authService.forgotPasswordSendOtp(email);
        return ResponseEntity.ok("OTP sent to email");
    }

    @PostMapping("/verify-forgot-password")
    public ResponseEntity<String> verifyForgotPassword(
            @RequestBody VerifyForgotPasswordDto dto) {

        authService.verifyForgotPasswordOtp(
                dto.getEmail(),
                dto.getOtp(),
                dto.getResetPassword()
        );

        return ResponseEntity.ok("Password reset successful");
    }
}


