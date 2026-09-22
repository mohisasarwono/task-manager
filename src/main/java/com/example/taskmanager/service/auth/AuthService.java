package com.example.taskmanager.service.auth;

import com.example.taskmanager.dto.entity.UserDTO;
import com.example.taskmanager.dto.request.auth.LoginRequest;
import com.example.taskmanager.dto.request.auth.RegisterRequest;
import com.example.taskmanager.dto.response.GenericResponse;
import com.example.taskmanager.dto.response.auth.LoginResponse;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.security.JwtService;
import com.example.taskmanager.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public GenericResponse<UserDTO> register(RegisterRequest registerReq){
        userRepository.findByEmail(registerReq.getEmail())
                .ifPresent(u -> {throw new BadCredentialsException("Email already exists");});

        User toBeCreatedUser = modelMapper.map(registerReq, User.class);
        toBeCreatedUser.setPassword(passwordEncoder.encode(registerReq.getPassword()));

        User savedUser = userRepository.save(toBeCreatedUser);
        return new GenericResponse<>(modelMapper.map(savedUser, UserDTO.class));
    }

    public GenericResponse<LoginResponse> login(LoginRequest loginReq){
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword());

        Authentication authentication = authenticationManager.authenticate(token);
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        User user = Objects.requireNonNull(myUserDetails).user();
        return new GenericResponse<>(
                new LoginResponse()
                        .setId(user.getId())
                        .setToken(jwtService.generateAccessToken(user))
                        .setRefreshToken(jwtService.generateRefreshToken(user))
        );
    }

    public GenericResponse<LoginResponse> refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromRefreshToken(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new BadCredentialsException("Invalid token"));
        String accessToken = jwtService.generateAccessToken(user);
        return new GenericResponse<>(
                new LoginResponse()
                        .setId(user.getId())
                        .setToken(accessToken)
                        .setRefreshToken(refreshToken)
        );
    }
}
