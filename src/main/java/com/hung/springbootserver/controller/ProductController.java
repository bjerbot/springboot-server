package com.hung.springbootserver.controller;

import com.hung.springbootserver.dto.ProductQueryParams;
import com.hung.springbootserver.dto.ProductRequest;
import com.hung.springbootserver.model.Product;
import com.hung.springbootserver.service.ProductService;
import com.hung.springbootserver.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    //查詢所有商品Controller層
    @Validated
    @GetMapping("/products")
    public ResponseEntity<Page<Product>> queryProducts(
            //查詢條件 Filtering
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            //排序 Sorting
            @RequestParam(defaultValue = "product_id") String orderBy,
            @RequestParam(defaultValue = "asc") String sort,
            //分頁 Pagination
            @RequestParam(defaultValue = "8") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
            ){

        ProductQueryParams productQueryParams =new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

        Page<Product> page= productService.queryProducts(productQueryParams);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    //查詢商品Controller層
    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> queryProductById(@PathVariable Integer productId){
        Product product = productService.queryProductById(productId);
        if(product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //新增商品Controller層
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
        Product newProduct = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }


    //修改商品Controller層
    @PutMapping("/products/{productId}")
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
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<String> deleteProductById(@PathVariable Integer productId){
        productService.deleteProductById(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
