package com.example.taskmanager.controller;

import com.example.taskmanager.dto.entity.TaskDTO;
import com.example.taskmanager.dto.request.task.TaskRequest;
import com.example.taskmanager.dto.response.GenericResponse;
import com.example.taskmanager.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping()
    public ResponseEntity<GenericResponse<List<TaskDTO>>> getListOfTaskDTO(
            @RequestParam(value = "status", required = false) String status){
        if (status != null) {
            return new ResponseEntity<>(taskService.listOfTasksByStatus(status), HttpStatus.OK);
        }
        return new ResponseEntity<>(taskService.listOfTasks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<TaskDTO>> getTaskById(@PathVariable("id") Long id){
        return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<GenericResponse<TaskDTO>> createTask(@RequestBody TaskRequest taskRequest){
        return new ResponseEntity<>(taskService.createTask(taskRequest),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<TaskDTO>> updateTask(@PathVariable("id") Long id,
                                                               @RequestBody TaskRequest taskRequest){
        return new ResponseEntity<>(taskService.updateTask(id,taskRequest),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<String>> deleteTask(@PathVariable("id") Long id){
        return new ResponseEntity<>(taskService.deleteTask(id),HttpStatus.OK);
    }


}
