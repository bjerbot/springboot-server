package com.hung.springbootserver.service;

import com.hung.springbootserver.dto.ProductQueryParams;
import com.hung.springbootserver.dto.ProductRequest;
import com.hung.springbootserver.model.Product;
import com.hung.springbootserver.util.Page;

import java.util.List;

public interface ProductService {

    Page<Product> queryProducts(ProductQueryParams productQueryParams);

    Product queryProductById(Integer productId);

    Product createProduct(ProductRequest productRequest);

    Product updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);
}
