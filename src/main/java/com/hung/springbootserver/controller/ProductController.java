package com.hung.springbootserver.controller;

import com.hung.springbootserver.dto.ProductRequest;
import com.hung.springbootserver.model.Product;
import com.hung.springbootserver.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequestMapping("/product")
@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    //新增商品Controller層
    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
        Product newProduct = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.OK).body(newProduct);
    }


    //查詢商品Controller層
    @GetMapping("/query/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer productId){
        Product product = productService.getProductById(productId);
        if(product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //修改商品Controller層
    @PutMapping("/update/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest){
        Product product = productService.updateProduct(productId, productRequest);
        if(product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //刪除商品Controller層
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer productId){
        Integer deleteId = productService.deleteProduct(productId);
        if(deleteId != null) {
            return ResponseEntity.status(HttpStatus.OK).body("成功刪除第" + productId + "項資料");
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("請求失敗，狀態碼：404");
        }
    }
}
