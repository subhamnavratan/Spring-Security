package com.Subham.PRASAG.config;

import com.Subham.PRASAG.service.JwtService;
import com.Subham.PRASAG.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // STEP 1: Read Authorization header
        final String authHeader = request.getHeader("Authorization");

        // STEP 2: If no Bearer token → continue filter chain
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // STEP 3: Extract JWT token
        final String jwt = authHeader.substring(7);
        final String username;

        try {
            // STEP 4: Extract username (email) from token
            username = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            // Invalid / malformed / expired token
            filterChain.doFilter(request, response);
            return;
        }

        // STEP 5: Authenticate only if context is empty
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            // STEP 6: Load user using CustomUserDetailsService
            var userDetails =
                    userDetailsService.loadUserByUsername(username);

            // STEP 7: Validate token against user
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // STEP 8: Create Authentication object
                /*🔹 What is Authentication object?
                It is just a note that says:“This request is from THIS user, and these are their permissions.”*/
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // STEP 9: Attach request details
                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // STEP 10: Set authentication in SecurityContext
                /*🔹 What is SecurityContext?
                   It is a temporary storage place where Spring Security keeps that ID
                                             card while the request is being processed.
                   Think of it like:“Who is logged in right now for this request?”*/
                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);
            }
        }

        // STEP 11: Continue filter chain
        filterChain.doFilter(request, response);
    }
}
