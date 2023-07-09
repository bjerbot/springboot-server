package com.hung.springbootserver.service.impl;

import com.hung.springbootserver.dao.ProductDao;
import com.hung.springbootserver.dto.ProductRequest;
import com.hung.springbootserver.model.Product;
import com.hung.springbootserver.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;

@Component
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductDao productDao;

    //新增商品Service層
    @PutMapping
    public Product createProduct(ProductRequest productRequest){
        Integer createId = productDao.createProduct(productRequest);
        System.out.println(createId);
        return productDao.getProductById(createId);
    }

    //查詢商品Service層
    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }

    //修改商品Service層
    @Override
    public Product updateProduct(Integer productId, ProductRequest productRequest) {
        Product checkDataExist = productDao.getProductById(productId);
        if(checkDataExist == null){
            return null;
        }else {
            Integer updatedId = productDao.updateProduct(productId, productRequest);
            return productDao.getProductById(updatedId);
        }
    }

    //刪除商品Service層
    @Override
    public Integer deleteProduct(Integer productId) {
        Product checkDataExist = productDao.getProductById(productId);
        if(checkDataExist == null){
            return null;
        }else{
            return productDao.deleteProduct(productId);
        }

    }
}
