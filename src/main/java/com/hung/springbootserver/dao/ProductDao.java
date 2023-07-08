package com.hung.springbootserver.dao;

import com.hung.springbootserver.dto.RequestProduct;
import com.hung.springbootserver.model.Product;
import org.springframework.http.ResponseEntity;

public interface ProductDao {

    Integer createProduct(RequestProduct requestProduct);
    Product getProductById(Integer productId);
}
