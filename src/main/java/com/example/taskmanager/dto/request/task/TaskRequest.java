package com.example.taskmanager.dto.request.task;

import com.example.taskmanager.static_enum.Priority;
import com.example.taskmanager.static_enum.Status;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@Accessors(chain = true)
public class TaskRequest {
    private String title;
    private String description;
    private Status status;
    private LocalDateTime dueDate;
    private Priority priority;
}
