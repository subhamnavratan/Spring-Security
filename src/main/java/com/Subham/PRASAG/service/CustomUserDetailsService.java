package com.Subham.PRASAG.service;

import com.Subham.PRASAG.config.CustomUserDetails;
import com.Subham.PRASAG.model.User;
import com.Subham.PRASAG.repo.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
/*Without UserDetailsService:
Spring Security can’t fetch users
Login cannot happen
UserDetailsService is the bridge between your database and Spring Security.*/
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email)
                );

        return new CustomUserDetails(user);
    }
}
