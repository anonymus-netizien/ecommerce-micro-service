package com.stschool.ecommerce.userservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponseDto<T> {

    private boolean success;
    private int status;
    private String message;
    private T data;
}
