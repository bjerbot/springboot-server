package com.hung.springbootserver.service;

import com.hung.springbootserver.dto.ProductRequest;
import com.hung.springbootserver.model.Product;

public interface ProductService {

    Product createProduct(ProductRequest productRequest);

    Product getProductById(Integer productId);

    Product updateProduct(Integer productId, ProductRequest productRequest);

    Integer deleteProduct(Integer productId);
}
