package com.hung.springbootserver.dao;

import com.hung.springbootserver.dto.ProductRequest;
import com.hung.springbootserver.model.Product;

public interface ProductDao {

    Integer createProduct(ProductRequest productRequest);
    Product getProductById(Integer productId);

    Integer updateProduct(Integer productId, ProductRequest productRequest);

    Integer deleteProduct(Integer productId);
}
