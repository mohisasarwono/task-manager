package com.example.taskmanager.dto.response.auth;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginResponse {
    private Long id;
    private String token;
    private String refreshToken;
}
