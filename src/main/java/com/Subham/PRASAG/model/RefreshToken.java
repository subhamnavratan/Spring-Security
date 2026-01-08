package com.Subham.PRASAG.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tokenId;

    @Column(nullable = false, length = 500)
    @NotBlank
    private String refreshToken;

    @Column(nullable = false)
    private Instant expirationTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    public RefreshToken() {
    }


    public RefreshToken(String refreshToken,
                        Instant expirationTime,
                        User user) {
        this.refreshToken = refreshToken;
        this.expirationTime = expirationTime;
        this.user = user;
    }

    public Integer getTokenId() {
        return tokenId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Instant getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Instant expirationTime) {
        this.expirationTime = expirationTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
