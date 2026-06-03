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
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable String id,
                                                             @Valid @RequestBody ProductRequestDto request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // --- Query endpoints ---

    @GetMapping("/availability/{isAvailable}")
    public ResponseEntity<List<ProductResponseDto>> getAllProductsByAvailability(@PathVariable boolean isAvailable) {
        return ResponseEntity.ok(productService.getAllProductsByAvailability(isAvailable));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    @GetMapping("/price-greater-than/{price}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByPriceGreaterThan(@PathVariable int price) {
        if (price < 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productService.getProductsByPriceGreaterThan(price));
    }

    @GetMapping("/manufactured-after/{year}")
    public ResponseEntity<List<ProductResponseDto>> getProductsManufacturedAfter(@PathVariable int year) {
        if (year <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productService.getProductsManufacturedAfter(year));
    }

    @GetMapping("/available-price-greater-than")
    public ResponseEntity<List<ProductResponseDto>> getAvailableProductsWithPriceGreaterThan(@RequestParam double price) {
        if (price < 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productService.getAvailableProductsWithPriceGreaterThan(price));
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllProductNames() {
        return ResponseEntity.ok(productService.getAllProductNames());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getDistinctCategories() {
        return ResponseEntity.ok(productService.getDistinctCategories());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalProductsCount() {
        return ResponseEntity.ok(productService.getTotalProductsCount());
    }

    @GetMapping("/exists/company/{company}")
    public ResponseEntity<Boolean> hasProductFromCompany(@PathVariable String company) {
        return ResponseEntity.ok(productService.hasProductFromCompany(company));
    }

    @GetMapping("/all-available")
    public ResponseEntity<Boolean> areAllProductsAvailable() {
        return ResponseEntity.ok(productService.areAllProductsAvailable());
    }

    @GetMapping("/sort/price-asc")
    public ResponseEntity<List<ProductResponseDto>> getProductsSortedByPriceAsc() {
        return ResponseEntity.ok(productService.getProductsSortedByPriceAsc());
    }

    @GetMapping("/sort/name-desc")
    public ResponseEntity<List<ProductResponseDto>> getProductsSortedByNameDesc() {
        return ResponseEntity.ok(productService.getProductsSortedByNameDesc());
    }

    @GetMapping("/top-expensive/{limit}")
    public ResponseEntity<List<ProductResponseDto>> getTopNMostExpensiveProducts(@PathVariable int limit) {
        if (limit <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productService.getTopNMostExpensiveProducts(limit));
    }

    @GetMapping("/top-expensive/by-category")
    public ResponseEntity<List<TopProductsByCategoryDto>> getTopThreeMostExpensiveProductsByCategory() {
        return ResponseEntity.ok(productService.getTopThreeMostExpensiveProductsByCategory());
    }

    @GetMapping("/inventory-value")
    public ResponseEntity<TotalInventoryValueDto> getTotalInventoryValue() {
        return ResponseEntity.ok(productService.getTotalInventoryValue());
    }

    @GetMapping("/average-price/by-category")
    public ResponseEntity<List<CategoryAveragePriceDto>> getAveragePriceByCategory() {
        return ResponseEntity.ok(productService.getAveragePriceByCategory());
    }

    @GetMapping("/count/by-category")
    public ResponseEntity<List<CategoryCountDto>> countProductsByCategory() {
        return ResponseEntity.ok(productService.countProductsByCategory());
    }

    @GetMapping("/grouped/by-category")
    public ResponseEntity<Map<String, List<ProductResponseDto>>> getProductsGroupedByCategory() {
        return ResponseEntity.ok(productService.getProductsGroupedByCategory());
    }

    @GetMapping("/grouped/by-company")
    public ResponseEntity<Map<String, List<ProductResponseDto>>> getProductsGroupedByCompany() {
        return ResponseEntity.ok(productService.getProductsGroupedByCompany());
    }

    @GetMapping("/partitioned/by-availability")
    public ResponseEntity<Map<Boolean, List<ProductResponseDto>>> partitionByAvailability() {
        return ResponseEntity.ok(productService.partitionByAvailability());
    }

    @GetMapping("/highest-price")
    public ResponseEntity<ProductResponseDto> getMostExpensiveProduct() {
        return productService.getMostExpensiveProduct()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/lowest-price")
    public ResponseEntity<ProductResponseDto> getLeastExpensiveProduct() {
        return productService.getLeastExpensiveProduct()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/first")
    public ResponseEntity<ProductResponseDto> findFirstProduct() {
        return productService.findFirstProduct()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/map-by-id")
    public ResponseEntity<Map<String, ProductResponseDto>> getProductMapById(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(productService.getProductMapById(page, size));
    }
}
