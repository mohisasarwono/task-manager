package com.example.taskmanager.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegisterRequest {
    @NotBlank(message = "Username must be filled!")
    private String username;

    @NotBlank(message = "Email must be filled!")
    private String email;

    @NotBlank(message = "Password must be filled!")
    @Size(min = 8, message = "Password length at least must be 8 characters!")
    private String password;
}
