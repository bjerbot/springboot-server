package com.hung.springbootserver.service.impl;

import com.hung.springbootserver.dao.ProductDao;
import com.hung.springbootserver.dto.ProductRequest;
import com.hung.springbootserver.model.Product;
import com.hung.springbootserver.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductDao productDao;

    //查詢指定數量商品Service層
    public List<Product> queryFewProducts(String count){
        return productDao.queryFewProducts(count);
    }

    //查詢所有商品Service層
    public List<Product> queryAllProducts(){
        return productDao.queryAllProducts();
    }

    //新增商品Service層
    @PutMapping
    public Product createProduct(ProductRequest productRequest){
        Integer createId = productDao.createProduct(productRequest);
        System.out.println(createId);
        return productDao.queryProductById(createId);
    }

    //查詢商品Service層
    @Override
    public Product queryProductById(Integer productId) {
        return productDao.queryProductById(productId);
    }

    //修改商品Service層
    @Override
    public Product updateProduct(Integer productId, ProductRequest productRequest) {
        Product checkDataExist = productDao.queryProductById(productId);
        if(checkDataExist == null){
            return null;
        }else {
            Integer updatedId = productDao.updateProduct(productId, productRequest);
            return productDao.queryProductById(updatedId);
        }
    }

    //刪除商品Service層
    @Override
    public void deleteProduct(Integer productId) {
            productDao.deleteProduct(productId);
    }
}
