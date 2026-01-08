package com.Subham.PRASAG.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "forgot_password")
public class ForgotPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fpid;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date expirationTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ForgotPassword() {
    }

    public ForgotPassword(Integer otp, Date expirationTime, User user) {
        this.otp = otp;
        this.expirationTime = expirationTime;
        this.user = user;
    }

    public Integer getFpid() {
        return fpid;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
