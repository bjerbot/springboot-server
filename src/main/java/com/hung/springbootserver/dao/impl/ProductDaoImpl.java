package com.hung.springbootserver.dao.impl;

import com.hung.springbootserver.constant.ProductCategory;
import com.hung.springbootserver.dao.ProductDao;
import com.hung.springbootserver.dto.RequestProduct;
import com.hung.springbootserver.model.Product;
import com.hung.springbootserver.rowmapper.ProductRpwMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    //新增商品Dao層
    public Integer createProduct(RequestProduct requestProduct){
        String sql = "INSERT INTO product(product_name, category, image_url, price, stock, description, created_date, last_modified_date) " +
                "VALUE(:productName, :category, :imageUrl, :price, :stock, :description, :createdDate, :lastModifiedDate)";

        HashMap<String, Object> map = new HashMap<>();
        map.put("productName", requestProduct.getProductName());
        map.put("category", requestProduct.getCategory().toString());
        map.put("imageUrl", requestProduct.getImageUrl());
        map.put("price", requestProduct.getPrice());
        map.put("stock", requestProduct.getStock());
        map.put("description", requestProduct.getDescription());

        Instant nowDateTime = Instant.now();
        map.put("createdDate", nowDateTime);
        map.put("lastModifiedDate", nowDateTime);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int numberOfRowsAffected = namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        System.out.println(numberOfRowsAffected);
        return keyHolder.getKey().intValue();
    }

    //查詢商品Dao層
    @Override
    public Product getProductById(Integer productId) {
        String sql = "SELECT product_id, product_name, category, image_url, " +
                "price, stock, description, created_date, last_modified_date\n" +
                "FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRpwMapper());
        if(productList.size() > 0) {
            return productList.get(0);
        }
        else{
            return null;
        }
    }
}
