package com.hung.springbootserver.dao;

import com.hung.springbootserver.dto.ProductRequest;
import com.hung.springbootserver.model.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductDao {

    List<Product> queryFewProducts(String count);
    List<Product> queryAllProducts();

    Integer createProduct(ProductRequest productRequest);
    Product queryProductById(Integer productId);

    Integer updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProduct(Integer productId);
}
