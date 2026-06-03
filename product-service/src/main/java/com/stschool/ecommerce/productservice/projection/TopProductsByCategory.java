package com.stschool.ecommerce.productservice.projection;

import com.stschool.ecommerce.productservice.document.Product;

import java.util.List;

public interface TopProductsByCategory {
    String getCategory();
    List<Product> getTopProducts();
}
