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

import java.util.List;

@Validated
@RequestMapping("/product")
@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    //查詢所有商品Controller層
    @GetMapping("/query_count/{count}")
    public ResponseEntity<List<Product>> queryFewProducts(@PathVariable String count){
        List<Product> productList= productService.queryFewProducts(count);
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    //查詢所有商品Controller層
    @GetMapping("/queryAll")
    public ResponseEntity<List<Product>> queryAllProducts(){
        List<Product> productList= productService.queryAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    //新增商品Controller層
    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
        Product newProduct = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.OK).body(newProduct);
    }


    //查詢商品Controller層
    @GetMapping("/query/{productId}")
    public ResponseEntity<Product> queryProductById(@PathVariable Integer productId){
        Product product = productService.queryProductById(productId);
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
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
