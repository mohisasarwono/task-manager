package com.example.taskmanager.dto.request.task;

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
    private String status;
    private LocalDateTime dueDate;
    private String priority;
}
