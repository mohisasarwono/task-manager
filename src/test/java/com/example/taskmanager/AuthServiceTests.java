package com.example.taskmanager;

import com.example.taskmanager.dto.entity.UserDTO;
import com.example.taskmanager.dto.request.auth.LoginRequest;
import com.example.taskmanager.dto.request.auth.RegisterRequest;
import com.example.taskmanager.dto.response.GenericResponse;
import com.example.taskmanager.dto.response.auth.LoginResponse;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.security.JwtService;
import com.example.taskmanager.security.MyUserDetails;
import com.example.taskmanager.service.auth.AuthService;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    private User dummyUser;

    @BeforeEach
    void setup(){
        dummyUser = new User()
                .setId(1L)
                .setEmail("test@test.com")
                .setUsername("test.test")
                .setPassword("hashedPassword");
    }

    @Test
    void register_shouldReturnUserDTO_whenValidRegisterRequestIsValid(){
        RegisterRequest registerRequest = new RegisterRequest()
                .setEmail("test@test.com")
                .setUsername("test.test")
                .setPassword("plainPassword");

        UserDTO expectedUserDTO = new UserDTO()
                .setId(1L)
                .setEmail("test@test.com")
                .setUsername("test.test")
                .setPassword("hashedPassword");

        when(userRepository.findByEmail(registerRequest.getEmail()))
                .thenReturn(Optional.empty());
        when(modelMapper.map(registerRequest, User.class))
                .thenReturn(dummyUser);
        when(passwordEncoder.encode(registerRequest.getPassword()))
                .thenReturn("hashedPassword");
        when(userRepository.save(any(User.class)))
                .thenReturn(dummyUser);
        when(modelMapper.map(dummyUser, UserDTO.class))
                .thenReturn(expectedUserDTO);

        GenericResponse<UserDTO> result = authService.register(registerRequest);
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(1L, result.getData().getId());
        assertEquals("test@test.com", result.getData().getEmail());

        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_shouldThrowBadCredentialsException_whenEmailAlreadyExist(){
        RegisterRequest registerRequest = new RegisterRequest()
                .setEmail("test@test.com")
                .setUsername("test.test")
                .setPassword("plainPassword");

        when(userRepository.findByEmail(registerRequest.getEmail()))
                .thenReturn(Optional.of(dummyUser));

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authService.register(registerRequest)
        );

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_shouldReturnLoginResponse_whenCredentialsAreValid() {
        LoginRequest loginRequest = new LoginRequest()
                .setEmail("test@test.com")
                .setPassword("plainPassword");

        MyUserDetails dummyUserDetails = new MyUserDetails(dummyUser);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(dummyUserDetails);

        when(jwtService.generateAccessToken(dummyUser))
                .thenReturn("dummy-access-token");
        when(jwtService.generateRefreshToken(dummyUser))
                .thenReturn("dummy-refresh-token");

        GenericResponse<LoginResponse> result = authService.login(loginRequest);

        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(1L, result.getData().getId());
        assertEquals("dummy-access-token", result.getData().getToken());
        assertEquals("dummy-refresh-token", result.getData().getRefreshToken());

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
