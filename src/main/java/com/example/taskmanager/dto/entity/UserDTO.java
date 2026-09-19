package com.example.taskmanager.dto.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private String password;
}
