package com.hung.springbootserver.service;

import com.hung.springbootserver.dto.RequestProduct;
import com.hung.springbootserver.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

public interface ProductService {

    Product createProduct(RequestProduct requestProduct);

    Product getProductById(Integer productId);
}
