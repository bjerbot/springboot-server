package com.hung.springbootserver.dao;

import com.hung.springbootserver.dto.ProductQueryParams;
import com.hung.springbootserver.dto.ProductRequest;
import com.hung.springbootserver.model.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductDao {

    List<Product> queryProducts(ProductQueryParams productQueryParams);

    Integer countProduct(ProductQueryParams productQueryParams);

    Product queryProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    Integer updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);
}
