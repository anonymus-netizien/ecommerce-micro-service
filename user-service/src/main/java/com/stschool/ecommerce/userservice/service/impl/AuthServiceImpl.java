package com.stschool.ecommerce.userservice.service.impl;

import com.stschool.ecommerce.userservice.dto.request.LoginRequestDto;
import com.stschool.ecommerce.userservice.dto.request.UserRequestDto;
import com.stschool.ecommerce.userservice.dto.response.AuthResponseDto;
import com.stschool.ecommerce.userservice.dto.response.UserResponseDto;
import com.stschool.ecommerce.userservice.entity.User;
import com.stschool.ecommerce.userservice.exception.InvalidCredentialsException;
import com.stschool.ecommerce.userservice.exception.UserExistsException;
import com.stschool.ecommerce.userservice.exception.UserNotFoundException;
import com.stschool.ecommerce.userservice.repository.UserRepository;
import com.stschool.ecommerce.userservice.security.JwtUtil;
import com.stschool.ecommerce.userservice.service.AuthService;
import com.stschool.ecommerce.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public AuthResponseDto login(LoginRequestDto loginRequestDto) throws UserNotFoundException, InvalidCredentialsException {

        User user = userService.findByEmail(loginRequestDto.getEmail());

        boolean isValidPassword = passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword());

        if (!isValidPassword) {
            throw new InvalidCredentialsException("Invalid password");
        }

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        user.setLastLoggedAt(LocalDateTime.now());
        userRepository.save(user);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(mapToUserResponse(user))
                .build();
    }

    @Override
    public UserResponseDto signup(UserRequestDto userRequestDto) throws UserExistsException {

        if (userService.existsByEmail(userRequestDto.getEmail())) {
            throw new UserExistsException("user with email " + userRequestDto.getEmail() + " already exists");
        }

        User user = modelMapper.map(userRequestDto, User.class);

        return userService.save(user);
    }

    private UserResponseDto mapToUserResponse(User user) {
        return modelMapper.map(user, UserResponseDto.class);
    }

}
