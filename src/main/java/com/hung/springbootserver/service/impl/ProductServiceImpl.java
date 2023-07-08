package com.hung.springbootserver.service.impl;

import com.hung.springbootserver.dao.ProductDao;
import com.hung.springbootserver.dto.RequestProduct;
import com.hung.springbootserver.model.Product;
import com.hung.springbootserver.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductDao productDao;

    //新增商品Service層
    @PutMapping
    public Product createProduct(RequestProduct requestProduct){
        Integer createId = productDao.createProduct(requestProduct);
        System.out.println(createId);
        return productDao.getProductById(createId);
    }

    //查詢商品Service層
    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }
}
