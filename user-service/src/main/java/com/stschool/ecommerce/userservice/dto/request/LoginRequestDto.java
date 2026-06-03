package com.stschool.ecommerce.userservice.dto.request;

import lombok.Data;

@Data
public class LoginRequestDto {

    private String email;
    private String password;
}
