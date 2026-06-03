package com.stschool.ecommerce.productservice.service;

import com.stschool.ecommerce.productservice.dto.request.ProductRequestDto;
import com.stschool.ecommerce.productservice.dto.response.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductService {

    // --- CRUD ---

    ProductResponseDto createProduct(ProductRequestDto request);

    ProductResponseDto getProductById(String id);

    List<ProductResponseDto> getAllProducts();

    ProductResponseDto updateProduct(String id, ProductRequestDto request);

    void deleteProduct(String id);

    // --- Query methods ---

    List<ProductResponseDto> getAllProductsByAvailability(boolean isAvailable);

    List<ProductResponseDto> getProductsByCategory(String category);

    List<ProductResponseDto> getProductsByPriceGreaterThan(int price);

    List<String> getAllProductNames();

    long getTotalProductsCount();

    boolean hasProductFromCompany(String company);

    boolean areAllProductsAvailable();

    Optional<ProductResponseDto> findFirstProduct();

    List<String> getDistinctCategories();

    List<ProductResponseDto> getTopNMostExpensiveProducts(int limit);

    List<ProductResponseDto> getProductsSortedByPriceAsc();

    List<ProductResponseDto> getProductsSortedByNameDesc();

    TotalInventoryValueDto getTotalInventoryValue();

    List<ProductResponseDto> getProductsManufacturedAfter(int year);

    List<ProductResponseDto> getAvailableProductsWithPriceGreaterThan(double price);

    List<CategoryCountDto> countProductsByCategory();

    Map<String, List<ProductResponseDto>> getProductsGroupedByCategory();

    Map<String, List<ProductResponseDto>> getProductsGroupedByCompany();

    Map<Boolean, List<ProductResponseDto>> partitionByAvailability();

    Optional<ProductResponseDto> getMostExpensiveProduct();

    Optional<ProductResponseDto> getLeastExpensiveProduct();

    Map<String, ProductResponseDto> getProductMapById(int page, int size);

    List<CategoryAveragePriceDto> getAveragePriceByCategory();

    List<TopProductsByCategoryDto> getTopThreeMostExpensiveProductsByCategory();
}
