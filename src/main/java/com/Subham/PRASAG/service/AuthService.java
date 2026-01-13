package com.Subham.PRASAG.service;

import com.Subham.PRASAG.config.CustomUserDetails;
import com.Subham.PRASAG.dto.*;
import com.Subham.PRASAG.exception.OTPException;
import com.Subham.PRASAG.exception.UserExistsException;
import com.Subham.PRASAG.model.*;
import com.Subham.PRASAG.repo.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final SignupOtpRepository signupOtpRepository;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository,
                       SignupOtpRepository signupOtpRepository,
                       ForgotPasswordRepository forgotPasswordRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       EmailService emailService) {

        this.userRepository = userRepository;
        this.signupOtpRepository = signupOtpRepository;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    // ===================== SIGNUP =====================

    public void signupSendOtp(Signupdto dto) {

        if (userRepository.existsByEmail(dto.getEmail()))
            throw new UserExistsException("Email already registered");

        if (userRepository.existsByRollNO(dto.getRollNO()))
            throw new UserExistsException("Roll number already registered");

        int otp = generateOtp();

        signupOtpRepository.save(
                new SignupOtp(otp, otpExpiry(), dto.getEmail())
        );

        emailService.sendEmail(
                dto.getEmail(),
                "Email Verification for Signup",
                "OTP is: " + otp
        );
    }

    public void verifySignupOtp(String email, Integer otp, Signupdto dto) {

        SignupOtp signupOtp = signupOtpRepository.findByEmail(email)
                .orElseThrow(() -> new OTPException("OTP not found"));

        validateOtp(signupOtp.getOtp(), signupOtp.getExpirationTime(), otp);

        User user = new User(
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getRollNO(),
                dto.getName(),
                dto.getRole()
        );

        userRepository.save(user);
        signupOtpRepository.delete(signupOtp);

        emailService.sendEmail(
                user.getEmail(),
                "Signup Successful",
                "Account created successfully"
        );
    }

    public AuthResponse login(Logindto logindto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        logindto.getData(),
                        logindto.getPassword()
                )
        );

        User user = userRepository.findByEmail(logindto.getData())
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with email: " + logindto.getData()
                        )
                );

        // ACCESS TOKEN
        String accessToken = jwtService.generateToken(
                new CustomUserDetails(user)
        );

        // REFRESH TOKEN
        RefreshToken refreshToken = createRefreshToken(user);

        AuthResponse response = new AuthResponse();
        response.setToken(accessToken);
        response.setRefreshToken(refreshToken.getRefreshToken());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());

        return response;
    }


    public AuthResponse refreshAccessToken(RefreshTokenRequest request) {

        RefreshToken refreshToken =
                refreshTokenRepository.findByRefreshToken(request.getRefreshToken())
                        .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpirationTime().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        User user = refreshToken.getUser();

        String newAccessToken = jwtService.generateToken(
                new CustomUserDetails(user)
        );

        AuthResponse response = new AuthResponse();
        response.setToken(newAccessToken);
        response.setRefreshToken(refreshToken.getRefreshToken()); // 🔥 IMPORTANT
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());

        return response;
    }


    // ===================== FORGOT PASSWORD =====================

    public void forgotPasswordSendOtp(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int otp = generateOtp();

        forgotPasswordRepository.save(
                new ForgotPassword(otp, otpExpiry(), user)
        );

        emailService.sendEmail(
                user.getEmail(),
                "Password Reset",
                "OTP is: " + otp
        );
    }

    // 🔥 FINAL & CORRECT
    @Transactional
    public void verifyForgotPasswordOtp(
            String email,
            Integer otp,
            ResetPassword resetPassword) {

        ForgotPassword forgotPassword =
                forgotPasswordRepository.findByUserEmail(email)
                        .orElseThrow(() -> new RuntimeException("OTP not found"));

        validateOtp(
                forgotPassword.getOtp(),
                forgotPassword.getExpirationTime(),
                otp
        );

        User user = forgotPassword.getUser();

        // 🔴 BREAK REFRESH TOKEN RELATION (orphanRemoval handles delete)
        user.setRefreshToken(null);

        user.setPassword(
                passwordEncoder.encode(resetPassword.getNewPassword().trim())
        );

        userRepository.saveAndFlush(user);

        forgotPasswordRepository.delete(forgotPassword);
    }

    // ===================== HELPERS =====================

    private RefreshToken createRefreshToken(User user) {

        RefreshToken token = new RefreshToken(
                UUID.randomUUID().toString(),
                Instant.now().plusSeconds(7 * 24 * 60 * 60),
                user
        );

        // 🔥 SET BOTH SIDES OF RELATION
        user.setRefreshToken(token);

        return refreshTokenRepository.save(token);
    }

    private int generateOtp() {
        return 100000 + new Random().nextInt(900000);
    }

    private Date otpExpiry() {
        return new Date(System.currentTimeMillis() + 5 * 60 * 1000);
    }

    private void validateOtp(Integer saved, Date expiry, Integer input) {
        if (expiry.before(new Date()))
            throw new RuntimeException("OTP expired");
        if (!saved.equals(input))
            throw new RuntimeException("Invalid OTP");
    }
}
