package com.example.taskmanager.service.task;

import com.example.taskmanager.dto.entity.TaskDTO;
import com.example.taskmanager.dto.request.task.TaskRequest;
import com.example.taskmanager.dto.response.GenericResponse;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.helper.SecurityUtils;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.static_enum.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public GenericResponse<List<TaskDTO>> listOfTasks() {
        List<TaskDTO> listOfTask = taskRepository.findAllByUser(SecurityUtils.getCurrentUser())
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,"Something went wrong"
                        )
                ).stream().map(this::mapToTaskDTO).toList();
        return new GenericResponse<>(listOfTask);
    }

    public GenericResponse<List<TaskDTO>> listOfTasksByStatus(String status){
        List<TaskDTO> listOfTask = taskRepository.findAllByUserAndStatus(SecurityUtils.getCurrentUser(), Status.fromValue(status))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,"Something went wrong"
                        )
                ).stream().map(this::mapToTaskDTO).toList();
        return new GenericResponse<>(listOfTask);
    }

    public GenericResponse<TaskDTO> getTaskById(Long id){
        Task task =  getTaskByIdAndCurrUser(id);
        return new GenericResponse<>(modelMapper.map(task, TaskDTO.class));
    }

    public GenericResponse<TaskDTO> createTask(TaskRequest taskReq){
        Task tobeTask = modelMapper.map(taskReq, Task.class).setUser(SecurityUtils.getCurrentUser());
        Task savedTask = taskRepository.save(tobeTask);
        return new GenericResponse<>(modelMapper.map(savedTask, TaskDTO.class));
    }

    public GenericResponse<TaskDTO> updateTask(Long id,TaskRequest taskRequest){
        Task currTask = getTaskByIdAndCurrUser(id);

        if (taskRequest.getTitle() != null) currTask.setTitle(taskRequest.getTitle());
        if (taskRequest.getDescription() != null) currTask.setDescription(taskRequest.getDescription());
        if (taskRequest.getStatus() != null) currTask.setStatus(taskRequest.getStatus());
        if (taskRequest.getDueDate() != null) currTask.setDueDate(taskRequest.getDueDate());
        if (taskRequest.getPriority() != null) currTask.setPriority(taskRequest.getPriority());

        Task updatedTask = taskRepository.save(currTask);
        return new GenericResponse<>(modelMapper.map(updatedTask,TaskDTO.class));
    }

    public GenericResponse<String> deleteTask(Long id){
        taskRepository.delete(getTaskByIdAndCurrUser(id));
        return new GenericResponse<>("Task with id: "+id+" has been deleted successfully");
    }

    private Task getTaskByIdAndCurrUser(Long id){
        return taskRepository.findByIdAndUser(id, SecurityUtils.getCurrentUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cannot find task with id: "+id));
    }

    private TaskDTO mapToTaskDTO(Task task){
        return modelMapper.map(task, TaskDTO.class);
    }

}
