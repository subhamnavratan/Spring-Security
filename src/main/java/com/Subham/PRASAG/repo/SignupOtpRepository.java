package com.Subham.PRASAG.repo;

import com.Subham.PRASAG.model.SignupOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignupOtpRepository
        extends JpaRepository<SignupOtp, Integer> {

    Optional<SignupOtp> findByEmail(String email);

    void deleteByEmail(String email);
}
