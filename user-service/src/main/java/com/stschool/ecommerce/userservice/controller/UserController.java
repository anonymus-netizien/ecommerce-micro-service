package com.stschool.ecommerce.userservice.controller;

import com.stschool.ecommerce.userservice.dto.request.UserRequestDto;
import com.stschool.ecommerce.userservice.dto.response.ApiResponseDto;
import com.stschool.ecommerce.userservice.dto.response.UserResponseDto;
import com.stschool.ecommerce.userservice.exception.UserNotFoundException;
import com.stschool.ecommerce.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsers() {
        return ResponseEntity.ok(
                ApiResponseDto.<List<UserResponseDto>>builder()
                        .success(true)
                        .message("Users retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(userService.getAllUsers())
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getUserById(@PathVariable Long id) throws UserNotFoundException {
        return ResponseEntity.ok(
                ApiResponseDto.<UserResponseDto>builder()
                        .success(true)
                        .message("User retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(userService.getUserById(id))
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> update(@PathVariable Long id,
                                                                  @Valid @RequestBody UserRequestDto userRequestDto) throws UserNotFoundException {
        return ResponseEntity.ok(
                ApiResponseDto.<UserResponseDto>builder()
                        .success(true)
                        .message("User updated successfully")
                        .status(HttpStatus.OK.value())
                        .data(userService.update(id, userRequestDto))
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteById(@PathVariable Long id) throws UserNotFoundException {
        userService.deleteById(id);
        return ResponseEntity.ok(
                ApiResponseDto.<Void>builder()
                        .success(true)
                        .message("User deleted successfully")
                        .status(HttpStatus.OK.value())
                        .build());
    }
}
