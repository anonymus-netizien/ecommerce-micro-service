package com.stschool.ecommerce.productservice.document;

import com.stschool.ecommerce.productservice.enums.Category;
import com.stschool.ecommerce.productservice.enums.ProductStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String description;

    private String company;

    private Integer manufacturedYear;

    private BigDecimal price;

    private Integer stockQuantity;

    private Category category;

    private ProductStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
