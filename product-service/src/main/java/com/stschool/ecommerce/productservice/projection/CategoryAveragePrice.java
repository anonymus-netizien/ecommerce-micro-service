package com.stschool.ecommerce.productservice.projection;

import java.math.BigDecimal;

public interface CategoryAveragePrice {
    String getCategory();
    BigDecimal getAvgPrice();
}
