package com.stschool.ecommerce.productservice.dto.response;

import com.stschool.ecommerce.productservice.enums.Category;
import com.stschool.ecommerce.productservice.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

    private String id;
    private String name;
    private BigDecimal price;
    private Integer stockQuantity;
    private Category category;
    private String company;
    private ProductStatus status;
    private String description;
    private Integer manufacturedYear;
}
