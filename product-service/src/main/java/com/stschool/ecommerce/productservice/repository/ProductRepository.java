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

    @Query("{ 'status': ?0 }")
    List<Product> findByStatus(ProductStatus status);

    @Query("{ 'category': ?0 }")
    List<Product> findByCategory(String category);

    List<Product> findByPriceGreaterThan(BigDecimal price);

    List<Product> findByOrderByPriceDesc(PageRequest pageable);

    List<Product> findByOrderByPriceAsc();

    List<Product> findByOrderByNameDesc();

    Optional<Product> findTopByOrderByPriceDesc();

    Optional<Product> findTopByOrderByPriceAsc();

    List<Product> findByStatusAndPriceGreaterThan(ProductStatus status, BigDecimal price);

    List<Product> findByManufacturedYearAfter(int year);

    // --- Company exists (case-insensitive) ---

    @Query(value = "{ 'company': { $regex: ?0, $options: 'i' } }", exists = true)
    boolean existsByCompany(String company);

    // --- Count of non-active products (for areAllProductsAvailable check) ---

    @Query(value = "{ 'status': { $ne: 'ACTIVE' } }", count = true)
    long countByStatusNotActive();

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

    // --- Aggregation: total inventory value ---

    @Aggregation(pipeline = {
            "{ $group: { _id: null, total: { $sum: '$price' } } }",
            "{ $project: { _id: 0, total: 1 } }"
    })
    List<TotalInventoryValue> totalInventoryValue();

    // --- Aggregation: top 3 most expensive by category ---

    @Aggregation(pipeline = {
            "{ $sort: { price: -1 } }",
            "{ $group: { _id: '$category', products: { $push: '$$ROOT' } } }",
            "{ $project: { _id: 0, category: '$_id', topProducts: { $slice: ['$products', 3] } } }"
    })
    List<TopProductsByCategory> topThreeMostExpensiveByCategory();

    // --- Pagination ---

    Page<Product> findAll(Pageable pageable);

}
