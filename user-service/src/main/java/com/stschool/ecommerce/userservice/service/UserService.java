package com.stschool.ecommerce.userservice.service;

import com.stschool.ecommerce.userservice.dto.request.UserRequestDto;
import com.stschool.ecommerce.userservice.dto.response.UserResponseDto;
import com.stschool.ecommerce.userservice.entity.User;
import com.stschool.ecommerce.userservice.exception.UserExistsException;
import com.stschool.ecommerce.userservice.exception.UserNotFoundException;

import java.util.List;

public interface UserService {

    UserResponseDto save(User user) throws UserExistsException;

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long id) throws UserNotFoundException;

    UserResponseDto update(Long id, UserRequestDto userRequestDto) throws UserNotFoundException;

    void deleteById(Long id) throws UserNotFoundException;

    User findByEmail(String email) throws UserNotFoundException;

    boolean existsByEmail(String email);
}
