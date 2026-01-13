package com.Subham.PRASAG.dto;

public class AuthResponse {

    private String token;
    private String refreshToken;
    private String role;
    private String email;

    public AuthResponse() {
    }

    public AuthResponse(String token, String refreshToken, String email, String role) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.email = email;
        this.role = role;
    }

    // ================= GETTERS & SETTERS =================

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
