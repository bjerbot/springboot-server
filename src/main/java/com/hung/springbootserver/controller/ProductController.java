package com.hung.springbootserver.controller;

import com.hung.springbootserver.dto.RequestProduct;
import com.hung.springbootserver.model.Product;
import com.hung.springbootserver.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    //新增商品Controller層
    @PostMapping("/create_product")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid RequestProduct requestProduct){
        Product newProduct = productService.createProduct(requestProduct);
        return ResponseEntity.status(HttpStatus.OK).body(newProduct);
    }


    //查詢商品Controller層
    @GetMapping("/product/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer productId){
        Product product = productService.getProductById(productId);
        if(product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
