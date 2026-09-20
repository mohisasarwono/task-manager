package com.example.taskmanager.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginRequest {
    @Email
    @NotBlank(message = "Email must be filled!")
    private String email;

    @NotBlank()
    @Size(min = 8, message = "Password length must be greater or equals to 8")
    private String password;
}
