package com.tologram.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class AuthRequest {
    
    @NotBlank(message = "Username or email is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
}