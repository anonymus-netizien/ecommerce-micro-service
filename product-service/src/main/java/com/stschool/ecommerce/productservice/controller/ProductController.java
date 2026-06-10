package com.stschool.ecommerce.productservice.controller;

import com.stschool.ecommerce.productservice.dto.request.ProductRequestDto;
import com.stschool.ecommerce.productservice.dto.response.*;
import com.stschool.ecommerce.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // --- CRUD ---

    @PostMapping
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> createProduct(@Valid @RequestBody ProductRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDto.<ProductResponseDto>builder()
                        .success(true)
                        .message("Product created successfully")
                        .status(HttpStatus.CREATED.value())
                        .data(productService.createProduct(request))
                        .build());
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> getAllProducts() {
        return ResponseEntity.ok(
                ApiResponseDto.<List<ProductResponseDto>>builder()
                        .success(true)
                        .message("Products retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getAllProducts())
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(
                ApiResponseDto.<ProductResponseDto>builder()
                        .success(true)
                        .message("Product retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getProductById(id))
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> updateProduct(@PathVariable String id,
                                                                            @Valid @RequestBody ProductRequestDto request) {
        return ResponseEntity.ok(
                ApiResponseDto.<ProductResponseDto>builder()
                        .success(true)
                        .message("Product updated successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.updateProduct(id, request))
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(
                ApiResponseDto.<Void>builder()
                        .success(true)
                        .message("Product deleted successfully")
                        .status(HttpStatus.OK.value())
                        .build());
    }

    // --- Query endpoints ---

    @GetMapping("/availability/{isAvailable}")
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> getAllProductsByAvailability(@PathVariable boolean isAvailable) {
        return ResponseEntity.ok(
                ApiResponseDto.<List<ProductResponseDto>>builder()
                        .success(true)
                        .message("Products retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getAllProductsByAvailability(isAvailable))
                        .build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> getProductsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(
                ApiResponseDto.<List<ProductResponseDto>>builder()
                        .success(true)
                        .message("Products retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getProductsByCategory(category))
                        .build());
    }

    @GetMapping("/price-greater-than/{price}")
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> getProductsByPriceGreaterThan(@PathVariable int price) {
        if (price < 0) {
            return ResponseEntity.badRequest().body(
                    ApiResponseDto.<List<ProductResponseDto>>builder()
                            .success(false)
                            .message("Price must be non-negative")
                            .status(HttpStatus.BAD_REQUEST.value())
                            .build());
        }
        return ResponseEntity.ok(
                ApiResponseDto.<List<ProductResponseDto>>builder()
                        .success(true)
                        .message("Products retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getProductsByPriceGreaterThan(price))
                        .build());
    }

    @GetMapping("/manufactured-after/{year}")
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> getProductsManufacturedAfter(@PathVariable int year) {
        if (year <= 0) {
            return ResponseEntity.badRequest().body(
                    ApiResponseDto.<List<ProductResponseDto>>builder()
                            .success(false)
                            .message("Year must be positive")
                            .status(HttpStatus.BAD_REQUEST.value())
                            .build());
        }
        return ResponseEntity.ok(
                ApiResponseDto.<List<ProductResponseDto>>builder()
                        .success(true)
                        .message("Products retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getProductsManufacturedAfter(year))
                        .build());
    }

    @GetMapping("/available-price-greater-than")
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> getAvailableProductsWithPriceGreaterThan(@RequestParam double price) {
        if (price < 0) {
            return ResponseEntity.badRequest().body(
                    ApiResponseDto.<List<ProductResponseDto>>builder()
                            .success(false)
                            .message("Price must be non-negative")
                            .status(HttpStatus.BAD_REQUEST.value())
                            .build());
        }
        return ResponseEntity.ok(
                ApiResponseDto.<List<ProductResponseDto>>builder()
                        .success(true)
                        .message("Products retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getAvailableProductsWithPriceGreaterThan(price))
                        .build());
    }

    @GetMapping("/names")
    public ResponseEntity<ApiResponseDto<List<String>>> getAllProductNames() {
        return ResponseEntity.ok(
                ApiResponseDto.<List<String>>builder()
                        .success(true)
                        .message("Product names retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getAllProductNames())
                        .build());
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponseDto<List<String>>> getDistinctCategories() {
        return ResponseEntity.ok(
                ApiResponseDto.<List<String>>builder()
                        .success(true)
                        .message("Categories retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getDistinctCategories())
                        .build());
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponseDto<Long>> getTotalProductsCount() {
        return ResponseEntity.ok(
                ApiResponseDto.<Long>builder()
                        .success(true)
                        .message("Total count retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getTotalProductsCount())
                        .build());
    }

    @GetMapping("/exists/company/{company}")
    public ResponseEntity<ApiResponseDto<Boolean>> hasProductFromCompany(@PathVariable String company) {
        return ResponseEntity.ok(
                ApiResponseDto.<Boolean>builder()
                        .success(true)
                        .message("Check completed successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.hasProductFromCompany(company))
                        .build());
    }

    @GetMapping("/all-available")
    public ResponseEntity<ApiResponseDto<Boolean>> areAllProductsAvailable() {
        return ResponseEntity.ok(
                ApiResponseDto.<Boolean>builder()
                        .success(true)
                        .message("Check completed successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.areAllProductsAvailable())
                        .build());
    }

    @GetMapping("/sort/price-asc")
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> getProductsSortedByPriceAsc() {
        return ResponseEntity.ok(
                ApiResponseDto.<List<ProductResponseDto>>builder()
                        .success(true)
                        .message("Products retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getProductsSortedByPriceAsc())
                        .build());
    }

    @GetMapping("/sort/name-desc")
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> getProductsSortedByNameDesc() {
        return ResponseEntity.ok(
                ApiResponseDto.<List<ProductResponseDto>>builder()
                        .success(true)
                        .message("Products retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getProductsSortedByNameDesc())
                        .build());
    }

    @GetMapping("/top-expensive/{limit}")
    public ResponseEntity<ApiResponseDto<List<ProductResponseDto>>> getTopNMostExpensiveProducts(@PathVariable int limit) {
        if (limit <= 0) {
            return ResponseEntity.badRequest().body(
                    ApiResponseDto.<List<ProductResponseDto>>builder()
                            .success(false)
                            .message("Limit must be positive")
                            .status(HttpStatus.BAD_REQUEST.value())
                            .build());
        }
        return ResponseEntity.ok(
                ApiResponseDto.<List<ProductResponseDto>>builder()
                        .success(true)
                        .message("Top products retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getTopNMostExpensiveProducts(limit))
                        .build());
    }

    @GetMapping("/top-expensive/by-category")
    public ResponseEntity<ApiResponseDto<List<TopProductsByCategoryDto>>> getTopThreeMostExpensiveProductsByCategory() {
        return ResponseEntity.ok(
                ApiResponseDto.<List<TopProductsByCategoryDto>>builder()
                        .success(true)
                        .message("Top products by category retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getTopThreeMostExpensiveProductsByCategory())
                        .build());
    }

    @GetMapping("/inventory-value")
    public ResponseEntity<ApiResponseDto<TotalInventoryValueDto>> getTotalInventoryValue() {
        return ResponseEntity.ok(
                ApiResponseDto.<TotalInventoryValueDto>builder()
                        .success(true)
                        .message("Inventory value retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getTotalInventoryValue())
                        .build());
    }

    @GetMapping("/average-price/by-category")
    public ResponseEntity<ApiResponseDto<List<CategoryAveragePriceDto>>> getAveragePriceByCategory() {
        return ResponseEntity.ok(
                ApiResponseDto.<List<CategoryAveragePriceDto>>builder()
                        .success(true)
                        .message("Average prices retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getAveragePriceByCategory())
                        .build());
    }

    @GetMapping("/count/by-category")
    public ResponseEntity<ApiResponseDto<List<CategoryCountDto>>> countProductsByCategory() {
        return ResponseEntity.ok(
                ApiResponseDto.<List<CategoryCountDto>>builder()
                        .success(true)
                        .message("Category counts retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.countProductsByCategory())
                        .build());
    }

    @GetMapping("/grouped/by-category")
    public ResponseEntity<ApiResponseDto<Map<String, List<ProductResponseDto>>>> getProductsGroupedByCategory() {
        return ResponseEntity.ok(
                ApiResponseDto.<Map<String, List<ProductResponseDto>>>builder()
                        .success(true)
                        .message("Products grouped by category retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getProductsGroupedByCategory())
                        .build());
    }

    @GetMapping("/grouped/by-company")
    public ResponseEntity<ApiResponseDto<Map<String, List<ProductResponseDto>>>> getProductsGroupedByCompany() {
        return ResponseEntity.ok(
                ApiResponseDto.<Map<String, List<ProductResponseDto>>>builder()
                        .success(true)
                        .message("Products grouped by company retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getProductsGroupedByCompany())
                        .build());
    }

    @GetMapping("/partitioned/by-availability")
    public ResponseEntity<ApiResponseDto<Map<Boolean, List<ProductResponseDto>>>> partitionByAvailability() {
        return ResponseEntity.ok(
                ApiResponseDto.<Map<Boolean, List<ProductResponseDto>>>builder()
                        .success(true)
                        .message("Products partitioned by availability retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.partitionByAvailability())
                        .build());
    }

    @GetMapping("/highest-price")
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> getMostExpensiveProduct() {
        return productService.getMostExpensiveProduct()
                .map(product -> ResponseEntity.ok(
                        ApiResponseDto.<ProductResponseDto>builder()
                                .success(true)
                                .message("Most expensive product retrieved successfully")
                                .status(HttpStatus.OK.value())
                                .data(product)
                                .build()))
                .orElse(ResponseEntity.ok(
                        ApiResponseDto.<ProductResponseDto>builder()
                                .success(true)
                                .message("No product found")
                                .status(HttpStatus.OK.value())
                                .build()));
    }

    @GetMapping("/lowest-price")
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> getLeastExpensiveProduct() {
        return productService.getLeastExpensiveProduct()
                .map(product -> ResponseEntity.ok(
                        ApiResponseDto.<ProductResponseDto>builder()
                                .success(true)
                                .message("Least expensive product retrieved successfully")
                                .status(HttpStatus.OK.value())
                                .data(product)
                                .build()))
                .orElse(ResponseEntity.ok(
                        ApiResponseDto.<ProductResponseDto>builder()
                                .success(true)
                                .message("No product found")
                                .status(HttpStatus.OK.value())
                                .build()));
    }

    @GetMapping("/first")
    public ResponseEntity<ApiResponseDto<ProductResponseDto>> findFirstProduct() {
        return productService.findFirstProduct()
                .map(product -> ResponseEntity.ok(
                        ApiResponseDto.<ProductResponseDto>builder()
                                .success(true)
                                .message("First product retrieved successfully")
                                .status(HttpStatus.OK.value())
                                .data(product)
                                .build()))
                .orElse(ResponseEntity.ok(
                        ApiResponseDto.<ProductResponseDto>builder()
                                .success(true)
                                .message("No product found")
                                .status(HttpStatus.OK.value())
                                .build()));
    }

    @GetMapping("/map-by-id")
    public ResponseEntity<ApiResponseDto<Map<String, ProductResponseDto>>> getProductMapById(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(
                ApiResponseDto.<Map<String, ProductResponseDto>>builder()
                        .success(true)
                        .message("Product map retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(productService.getProductMapById(page, size))
                        .build());
    }
}
