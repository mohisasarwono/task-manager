package com.example.taskmanager.dto.entity;

import com.example.taskmanager.static_enum.Priority;
import com.example.taskmanager.static_enum.Status;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime dueDate;
    private Priority priority;
    private UserDTO user;
}
