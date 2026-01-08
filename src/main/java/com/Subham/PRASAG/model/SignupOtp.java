package com.Subham.PRASAG.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "signup_otp")
public class SignupOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date expirationTime;

    @Column(nullable = false, unique = true)
    private String email;

    public SignupOtp() {}

    public SignupOtp(Integer otp, Date expirationTime, String email) {
        this.otp = otp;
        this.expirationTime = expirationTime;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
