package com.stschool.ecommerce.userservice.controller;

import com.stschool.ecommerce.userservice.dto.request.UserRequestDto;
import com.stschool.ecommerce.userservice.dto.response.UserResponseDto;
import com.stschool.ecommerce.userservice.exception.UserNotFoundException;
import com.stschool.ecommerce.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @Valid @RequestBody UserRequestDto userRequestDto) throws UserNotFoundException {
        return ResponseEntity.ok(userService.update(id, userRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) throws UserNotFoundException {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }


}
