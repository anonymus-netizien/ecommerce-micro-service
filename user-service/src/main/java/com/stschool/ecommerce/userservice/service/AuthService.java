package com.stschool.ecommerce.userservice.service;

import com.stschool.ecommerce.userservice.dto.request.LoginRequestDto;
import com.stschool.ecommerce.userservice.dto.request.UserRequestDto;
import com.stschool.ecommerce.userservice.dto.response.AuthResponseDto;
import com.stschool.ecommerce.userservice.dto.response.UserResponseDto;
import com.stschool.ecommerce.userservice.exception.InvalidCredentialsException;
import com.stschool.ecommerce.userservice.exception.UserExistsException;
import com.stschool.ecommerce.userservice.exception.UserNotFoundException;

public interface AuthService {

    AuthResponseDto login(LoginRequestDto loginRequestDto) throws UserNotFoundException, InvalidCredentialsException;

    UserResponseDto signup(UserRequestDto userRequestDto) throws UserExistsException;
}
