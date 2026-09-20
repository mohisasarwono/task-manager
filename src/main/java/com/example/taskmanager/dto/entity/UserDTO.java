package com.example.taskmanager.dto.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties({"email","password"})
public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private String password;
}
