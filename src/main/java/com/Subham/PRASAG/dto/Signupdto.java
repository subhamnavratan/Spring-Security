package com.Subham.PRASAG.dto;

import com.Subham.PRASAG.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class Signupdto {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Roll number is required")
    private String rollNO;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }
    public Signupdto()
    {

    }

    public Signupdto(String name, String email, String rollNO, String password, Role role) {
        this.name = name;
        this.email = email;
        this.rollNO = rollNO;
        this.password = password;
        this.role = role;

    }

    public String getName() {
        return name;
    }


    public Role getRole() {
        return role;
    }


    public String getRollNO() {
        return rollNO;
    }

}
