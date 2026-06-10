package com.stschool.ecommerce.userservice.controller;

import com.stschool.ecommerce.userservice.dto.request.LoginRequestDto;
import com.stschool.ecommerce.userservice.dto.request.UserRequestDto;
import com.stschool.ecommerce.userservice.dto.response.ApiResponseDto;
import com.stschool.ecommerce.userservice.dto.response.AuthResponseDto;
import com.stschool.ecommerce.userservice.dto.response.UserResponseDto;
import com.stschool.ecommerce.userservice.exception.InvalidCredentialsException;
import com.stschool.ecommerce.userservice.exception.UserExistsException;
import com.stschool.ecommerce.userservice.exception.UserNotFoundException;
import com.stschool.ecommerce.userservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto)
            throws UserNotFoundException, InvalidCredentialsException {
        return ResponseEntity.ok(
                ApiResponseDto.<AuthResponseDto>builder()
                        .success(true)
                        .message("Login successful")
                        .status(HttpStatus.OK.value())
                        .data(authService.login(loginRequestDto))
                        .build());
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> signup(@Valid @RequestBody UserRequestDto signupRequestDto)
            throws UserExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDto.<UserResponseDto>builder()
                        .success(true)
                        .message("User registered successfully")
                        .status(HttpStatus.CREATED.value())
                        .data(authService.signup(signupRequestDto))
                        .build());
    }
}
