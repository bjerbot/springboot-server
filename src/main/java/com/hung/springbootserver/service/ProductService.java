package com.hung.springbootserver.service;

import com.hung.springbootserver.dto.ProductRequest;
import com.hung.springbootserver.model.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    List<Product> queryFewProducts(String count);
    List<Product> queryAllProducts();

    Product createProduct(ProductRequest productRequest);

    Product queryProductById(Integer productId);

    Product updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProduct(Integer productId);
}
