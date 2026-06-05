package com.stschool.ecommerce.productservice.repository;

import com.stschool.ecommerce.productservice.document.Product;
import com.stschool.ecommerce.productservice.enums.ProductStatus;
import com.stschool.ecommerce.productservice.projection.CategoryAveragePrice;
import com.stschool.ecommerce.productservice.projection.CategoryCount;
import com.stschool.ecommerce.productservice.projection.TopProductsByCategory;
import com.stschool.ecommerce.productservice.projection.TotalInventoryValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findByName(String name);

    Optional<Product> findTopByStatus(ProductStatus status);

    @Query("{ 'category': ?0 }")
    List<Product> findByCategory(String category);

    // --- Stock-based availability queries ---

    @Query(value = "{ 'stockQuantity': { $gt: 0 } }")
    List<Product> findAvailableProducts();

    @Query(value = "{ 'stockQuantity': 0 }")
    List<Product> findUnavailableProducts();

    @Query(value = "{ 'stockQuantity': { $gt: 0 }, 'price': { $gt: ?0 } }")
    List<Product> findAvailableProductsByPriceGreaterThan(BigDecimal price);

    // --- Price queries with explicit @Query for correct BigDecimal comparison ---

    @Query(value = "{ 'price': { $gt: ?0 } }")
    List<Product> findByPriceGreaterThan(BigDecimal price);

    @Query(value = "{ 'price': { $gt: ?0 } }", sort = "{ 'price': -1 }")
    List<Product> findByOrderByPriceDesc(PageRequest pageable);

    @Query(value = "{}", sort = "{ 'price': 1 }")
    List<Product> findByOrderByPriceAsc();

    @Query(value = "{}", sort = "{ 'name': -1 }")
    List<Product> findByOrderByNameDesc();

    @Query(value = "{}", sort = "{ 'price': -1 }")
    Optional<Product> findTopByOrderByPriceDesc();

    @Query(value = "{}", sort = "{ 'price': 1 }")
    Optional<Product> findTopByOrderByPriceAsc();

    List<Product> findByManufacturedYearAfter(int year);

    // --- Company exists (case-insensitive) ---

    @Query(value = "{ 'company': { $regex: ?0, $options: 'i' } }", exists = true)
    boolean existsByCompany(String company);

    // --- Count of unavailable products (stockQuantity == 0) ---

    @Query(value = "{ 'stockQuantity': 0 }", count = true)
    long countByStockZero();

    // --- Projection: product names only ---

    @Query(value = "{}", fields = "{ 'name': 1 }")
    List<Product> findAllNamesProjected();

    // --- Aggregation: count by category ---

    @Aggregation(pipeline = {
            "{ $group: { _id: '$category', count: { $sum: 1 } } }",
            "{ $project: { _id: 0, category: '$_id', count: 1 } }"
    })
    List<CategoryCount> countProductsByCategory();

    // --- Aggregation: average price by category ---

    @Aggregation(pipeline = {
            "{ $group: { _id: '$category', avgPrice: { $avg: '$price' } } }",
            "{ $project: { _id: 0, category: '$_id', avgPrice: 1 } }"
    })
    List<CategoryAveragePrice> averagePriceByCategory();

    // --- Aggregation: total inventory value (price * stockQuantity) ---

    @Aggregation(pipeline = {
            "{ $group: { _id: null, total: { $sum: { $multiply: ['$price', '$stockQuantity'] } } } }",
            "{ $project: { _id: 0, total: 1 } }"
    })
    List<TotalInventoryValue> totalInventoryValue();

    // --- Aggregation: top 3 most expensive by category ---

    @Aggregation(pipeline = {
            "{ $sort: { price: -1 } }",
            "{ $group: { _id: '$category', products: { $push: { 'id': '$_id', 'name': '$name', 'price': '$price', 'stockQuantity': '$stockQuantity', 'category': '$category', 'company': '$company', 'status': '$status', 'description': '$description', 'manufacturedYear': '$manufacturedYear' } } } }",
            "{ $project: { _id: 0, category: '$_id', topProducts: { $slice: ['$products', 3] } } }"
    })
    List<TopProductsByCategory> topThreeMostExpensiveByCategory();

    // --- Pagination ---

    Page<Product> findAll(Pageable pageable);

}
