package com.example.taskmanager;

import com.example.taskmanager.dto.entity.TaskDTO;
import com.example.taskmanager.dto.entity.UserDTO;
import com.example.taskmanager.dto.request.task.TaskRequest;
import com.example.taskmanager.dto.response.GenericResponse;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.helper.SecurityUtils;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.service.task.TaskService;
import com.example.taskmanager.static_enum.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TaskService taskService;

    private MockedStatic<SecurityUtils> securityUtilsMock;

    private User dummyUser;
    private Task dummyTask;

    @BeforeEach
    void setup(){
        dummyUser = new User()
                .setId(1L)
                .setUsername("mohi")
                .setEmail("mohi@example.com")
                .setPassword("hashedpassword");

        dummyTask = new Task()
                .setId(10L)
                .setTitle("Belajar Unit Test")
                .setDescription("Latihan bikin test pertama")
                .setStatus(Status.CREATED)
                .setUser(dummyUser);

        securityUtilsMock = Mockito.mockStatic(SecurityUtils.class);
        securityUtilsMock.when(SecurityUtils::getCurrentUser).thenReturn(dummyUser);
    }

    @AfterEach
    void tearDown(){
        securityUtilsMock.close();
    }

    @Test
    void getTaskId_shouldReturnTaskDTO_whenTaskExists(){
        TaskDTO expectedTaskDTO = new TaskDTO();
        expectedTaskDTO.setId(10L);
        expectedTaskDTO.setTitle("Belajar Unit Test");

        when(taskRepository.findByIdAndUser(10L, dummyUser))
                .thenReturn(Optional.of(dummyTask));
        when(modelMapper.map(dummyTask, TaskDTO.class))
                .thenReturn(expectedTaskDTO);

        GenericResponse<TaskDTO> result = taskService.getTaskById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getData().getId());
        assertEquals("Belajar Unit Test", result.getData().getTitle());
    }

    @Test
    void getTaskId_shouldThrowNotFound_whenTaskDoesNotExists(){
        when(taskRepository.findByIdAndUser(99L, dummyUser))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> taskService.getTaskById(99L)
        );

        assertTrue(Objects.requireNonNull(exception.getReason()).contains("Cannot find task with id: 99"));
    }

    @Test
    void createTask_shouldReturnSavedTaskDTO_whenTaskRequestValid(){
        TaskRequest taskRequest = new TaskRequest()
                .setTitle("Belajar Unit Test")
                .setDescription("Latihan bikin test pertama")
                .setStatus(Status.CREATED);

        UserDTO expectedUserDTO = new UserDTO()
                .setId(1L)
                .setUsername("mohi")
                .setEmail("mohi@example.com")
                .setPassword("hashedpassword");

        TaskDTO expectedTaskDTO = new TaskDTO()
                .setId(10L)
                .setTitle("Belajar Unit Test")
                .setDescription("Latihan bikin test pertama")
                .setStatus(Status.CREATED)
                .setUser(expectedUserDTO);

        when(modelMapper.map(taskRequest,Task.class))
                .thenReturn(dummyTask);
        when(taskRepository.save(any(Task.class)))
                .thenReturn(dummyTask);
        when(modelMapper.map(dummyTask,TaskDTO.class))
                .thenReturn(expectedTaskDTO);

        GenericResponse<TaskDTO> result = taskService.createTask(taskRequest);
        assertNotNull(result);
        assertEquals(10L, result.getData().getId());
        assertEquals("Belajar Unit Test", result.getData().getTitle());

    }

    @Test
    void updateTask_shouldReturnUpdatedDTO_whenIdAndTaskRequestValid(){
        Long id = 10L;
        TaskRequest taskRequest = new TaskRequest()
                .setTitle("Updated Title");

        UserDTO expectedUserDTO = new UserDTO()
                .setId(1L)
                .setUsername("mohi")
                .setEmail("mohi@example.com")
                .setPassword("hashedpassword");

        TaskDTO expectedTaskDTO = new TaskDTO()
                .setId(10L)
                .setTitle("Updated Title")
                .setDescription("Latihan bikin test pertama")
                .setStatus(Status.CREATED)
                .setUser(expectedUserDTO);

        when(taskRepository.findByIdAndUser(id,dummyUser))
                .thenReturn(Optional.of(dummyTask));
        when(taskRepository.save(any(Task.class)))
                .thenReturn(dummyTask);
        when(modelMapper.map(dummyTask,TaskDTO.class))
                .thenReturn(expectedTaskDTO);

        GenericResponse<TaskDTO> result = taskService.updateTask(id,taskRequest);
        assertNotNull(result);
        assertEquals(10L, result.getData().getId());
        assertEquals("Updated Title", result.getData().getTitle());
    }

    @Test
    void deleteTask_shouldReturnSuccessMessage_whenIdIsValid(){

        GenericResponse<String> expectedResponse = new GenericResponse<>("Task with id: 10 has been deleted successfully");

        when(taskRepository.findByIdAndUser(10L,dummyUser))
                .thenReturn(Optional.of(dummyTask));
        doNothing().when(taskRepository).delete(dummyTask);
        GenericResponse<String> result = taskService.deleteTask(10L);
        assertEquals("Task with id: 10 has been deleted successfully", result.getData());
    }
}
