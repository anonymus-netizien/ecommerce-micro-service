package com.stschool.ecommerce.productservice.service.impl;

import com.stschool.ecommerce.productservice.document.Product;
import com.stschool.ecommerce.productservice.dto.request.ProductRequestDto;
import com.stschool.ecommerce.productservice.dto.response.*;
import com.stschool.ecommerce.productservice.enums.ProductStatus;
import com.stschool.ecommerce.productservice.exception.ProductExistsException;
import com.stschool.ecommerce.productservice.exception.ProductNotFoundException;
import com.stschool.ecommerce.productservice.projection.TotalInventoryValue;
import com.stschool.ecommerce.productservice.repository.ProductRepository;
import com.stschool.ecommerce.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    // --- CRUD ---

    @Override
    public ProductResponseDto createProduct(ProductRequestDto request) {
        if (productRepository.findByName(request.getName()).isPresent()) {
            throw new ProductExistsException("Product with name '" + request.getName() + "' already exists");
        }

        Product product = modelMapper.map(request, Product.class);
        product.setStatus(ProductStatus.ACTIVE);
        product.setCreatedAt(LocalDateTime.now());

        Product saved = productRepository.save(product);
        return modelMapper.map(saved, ProductResponseDto.class);
    }

    @Override
    public ProductResponseDto getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
        return modelMapper.map(product, ProductResponseDto.class);
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .toList();
    }

    @Override
    public ProductResponseDto updateProduct(String id, ProductRequestDto request) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));

        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setCompany(request.getCompany());
        existing.setManufacturedYear(request.getManufacturedYear());
        existing.setPrice(request.getPrice());
        existing.setStockQuantity(request.getStockQuantity());
        existing.setCategory(request.getCategory());
        existing.setUpdatedAt(LocalDateTime.now());

        Product saved = productRepository.save(existing);
        return modelMapper.map(saved, ProductResponseDto.class);
    }

    @Override
    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found"));
        productRepository.delete(product);
    }

    // --- Query methods ---

    @Override
    public List<ProductResponseDto> getAllProductsByAvailability(boolean isAvailable) {
        if (isAvailable) {
            return productRepository.findByStatus(ProductStatus.ACTIVE)
                    .stream()
                    .map(p -> modelMapper.map(p, ProductResponseDto.class))
                    .toList();
        } else {
            return productRepository.findAll()
                    .stream()
                    .filter(p -> p.getStatus() != ProductStatus.ACTIVE)
                    .map(p -> modelMapper.map(p, ProductResponseDto.class))
                    .toList();
        }
    }

    @Override
    public List<ProductResponseDto> getProductsByCategory(String category) {
        return productRepository.findByCategory(category)
                .stream()
                .map(p -> modelMapper.map(p, ProductResponseDto.class))
                .toList();
    }

    @Override
    public List<ProductResponseDto> getProductsByPriceGreaterThan(int price) {
        return productRepository.findByPriceGreaterThan(BigDecimal.valueOf(price))
                .stream()
                .map(p -> modelMapper.map(p, ProductResponseDto.class))
                .toList();
    }

    @Override
    public List<String> getAllProductNames() {
        return productRepository.findAllNamesProjected()
                .stream()
                .map(Product::getName)
                .toList();
    }

    @Override
    public long getTotalProductsCount() {
        return productRepository.count();
    }

    @Override
    public boolean hasProductFromCompany(String company) {
        return productRepository.existsByCompany(company);
    }

    @Override
    public boolean areAllProductsAvailable() {
        return productRepository.countByStatusNotActive() == 0;
    }

    @Override
    public Optional<ProductResponseDto> findFirstProduct() {
        return productRepository.findTopByStatus(ProductStatus.ACTIVE)
                .map(product -> modelMapper.map(product, ProductResponseDto.class));
    }

    @Override
    public List<String> getDistinctCategories() {
        return productRepository.findAll()
                .stream()
                .map(Product::getCategory)
                .map(Enum::name)
                .distinct()
                .toList();
    }

    @Override
    public List<ProductResponseDto> getTopNMostExpensiveProducts(int limit) {
        return productRepository.findByOrderByPriceDesc(PageRequest.of(0, limit))
                .stream()
                .map(p -> modelMapper.map(p, ProductResponseDto.class))
                .toList();
    }

    @Override
    public List<ProductResponseDto> getProductsSortedByPriceAsc() {
        return productRepository.findByOrderByPriceAsc()
                .stream()
                .map(p -> modelMapper.map(p, ProductResponseDto.class))
                .toList();
    }

    @Override
    public List<ProductResponseDto> getProductsSortedByNameDesc() {
        return productRepository.findByOrderByNameDesc()
                .stream()
                .map(p -> modelMapper.map(p, ProductResponseDto.class))
                .toList();
    }

    @Override
    public TotalInventoryValueDto getTotalInventoryValue() {
        List<TotalInventoryValue> result = productRepository.totalInventoryValue();
        BigDecimal total = result.isEmpty() ? BigDecimal.ZERO : Optional.ofNullable(result.get(0).getTotal())
                .orElse(BigDecimal.ZERO);
        return TotalInventoryValueDto.builder().total(total).build();
    }

    @Override
    public List<ProductResponseDto> getProductsManufacturedAfter(int year) {
        return productRepository.findByManufacturedYearAfter(year)
                .stream()
                .map(p -> modelMapper.map(p, ProductResponseDto.class))
                .toList();
    }

    @Override
    public List<ProductResponseDto> getAvailableProductsWithPriceGreaterThan(double price) {
        return productRepository.findByStatusAndPriceGreaterThan(ProductStatus.ACTIVE, BigDecimal.valueOf(price))
                .stream()
                .map(p -> modelMapper.map(p, ProductResponseDto.class))
                .toList();
    }

    @Override
    public List<CategoryCountDto> countProductsByCategory() {
        return productRepository.countProductsByCategory()
                .stream()
                .map(projection -> CategoryCountDto.builder()
                        .category(projection.getCategory())
                        .count(projection.getCount())
                        .build())
                .toList();
    }

    @Override
    public Map<String, List<ProductResponseDto>> getProductsGroupedByCategory() {
        return productRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        p -> p.getCategory().name(),
                        Collectors.mapping(p -> modelMapper.map(p, ProductResponseDto.class),
                                Collectors.toList())
                ));
    }

    @Override
    public Map<String, List<ProductResponseDto>> getProductsGroupedByCompany() {
        return productRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Product::getCompany,
                        Collectors.mapping(p -> modelMapper.map(p, ProductResponseDto.class),
                                Collectors.toList())
                ));
    }

    @Override
    public Map<Boolean, List<ProductResponseDto>> partitionByAvailability() {
        return productRepository.findAll()
                .stream()
                .collect(Collectors.partitioningBy(
                        p -> p.getStatus() == ProductStatus.ACTIVE,
                        Collectors.mapping(p -> modelMapper.map(p, ProductResponseDto.class),
                                Collectors.toList())
                ));
    }

    @Override
    public Optional<ProductResponseDto> getMostExpensiveProduct() {
        return productRepository.findTopByOrderByPriceDesc()
                .map(product -> modelMapper.map(product, ProductResponseDto.class));
    }

    @Override
    public Optional<ProductResponseDto> getLeastExpensiveProduct() {
        return productRepository.findTopByOrderByPriceAsc()
                .map(product -> modelMapper.map(product, ProductResponseDto.class));
    }

    @Override
    public Map<String, ProductResponseDto> getProductMapById(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size))
                .stream()
                .collect(Collectors.toMap(
                        Product::getId,
                        product -> modelMapper.map(product, ProductResponseDto.class)
                ));
    }

    @Override
    public List<CategoryAveragePriceDto> getAveragePriceByCategory() {
        return productRepository.averagePriceByCategory()
                .stream()
                .map(projection -> CategoryAveragePriceDto.builder()
                        .category(projection.getCategory())
                        .avgPrice(projection.getAvgPrice())
                        .build())
                .toList();
    }

    @Override
    public List<TopProductsByCategoryDto> getTopThreeMostExpensiveProductsByCategory() {
        return productRepository.topThreeMostExpensiveByCategory()
                .stream()
                .map(projection -> TopProductsByCategoryDto.builder()
                        .category(projection.getCategory())
                        .topProducts(projection.getTopProducts()
                                .stream()
                                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                                .toList())
                        .build())
                .toList();
    }
}
