package com.stschool.ecommerce.productservice.dto.request;

import com.stschool.ecommerce.productservice.enums.Category;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDto {

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String company;

    @Min(value = 1900, message = "Manufactured year must be at least 1900")
    @Max(value = 2099, message = "Manufactured year must be at most 2099")
    private Integer manufacturedYear;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    @NotNull(message = "Category is required")
    private Category category;
}
