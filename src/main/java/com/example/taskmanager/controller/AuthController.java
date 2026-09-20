package com.example.taskmanager.controller;

import com.example.taskmanager.dto.entity.UserDTO;
import com.example.taskmanager.dto.request.auth.LoginRequest;
import com.example.taskmanager.dto.request.auth.RegisterRequest;
import com.example.taskmanager.dto.response.GenericResponse;
import com.example.taskmanager.dto.response.auth.LoginResponse;
import com.example.taskmanager.service.auth.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private  final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<UserDTO>> register(@Valid @RequestBody RegisterRequest registerReq){
        return new ResponseEntity<>(authService.register(registerReq), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest,
                                                                HttpServletResponse response){
        GenericResponse<LoginResponse> loginResponse = authService.login(loginRequest);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", loginResponse.getData().getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<GenericResponse<LoginResponse>> refresh(HttpServletRequest request){
        if(request.getCookies()==null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There's no cookie found. Please do login first");

        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equalsIgnoreCase(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("refreshToken not found"));
        return new ResponseEntity<>(authService.refreshToken(refreshToken), HttpStatus.OK);
    }
}
