package com.Subham.PRASAG.repo;

import com.Subham.PRASAG.model.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForgotPasswordRepository
        extends JpaRepository<ForgotPassword, Integer> {

    Optional<ForgotPassword> findByUserEmail(String email);

    void deleteByUserEmail(String email);
}
