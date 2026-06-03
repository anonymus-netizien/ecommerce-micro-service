package com.stschool.ecommerce.userservice.controller;

import com.stschool.ecommerce.userservice.dto.request.LoginRequestDto;
import com.stschool.ecommerce.userservice.dto.request.UserRequestDto;
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
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto)
            throws UserNotFoundException, InvalidCredentialsException {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody UserRequestDto signupRequestDto)
            throws UserExistsException {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(signupRequestDto));
    }
}
