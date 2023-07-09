package com.hung.springbootserver.dao.impl;

import com.hung.springbootserver.dao.ProductDao;
import com.hung.springbootserver.dto.ProductRequest;
import com.hung.springbootserver.model.Product;
import com.hung.springbootserver.rowmapper.ProductRpwMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Integer createProduct(ProductRequest productRequest){
        String sql = "INSERT INTO product(product_name, category, image_url, price, stock, description, created_date, last_modified_date) " +
                "VALUE(:productName, :category, :imageUrl, :price, :stock, :description, :createdDate, :lastModifiedDate)";

        HashMap<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

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

    //修改商品Dao層
    @Override
    public Integer updateProduct(Integer productId,  ProductRequest productRequest){
        String sql = "UPDATE product SET product_name=:productName, category=:category, image_url=:imageUrl, " +
                "price=:price, stock=:stock, description=:description, " +
                "last_modified_date=:lastModifiedDate WHERE product_id=:productId;";

        HashMap<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Instant nowDateTime = Instant.now();
        map.put("lastModifiedDate", nowDateTime);

        namedParameterJdbcTemplate.update(sql, map);
        return productId;
    }

    //刪除商品Dao層
    @Override
    public Integer deleteProduct(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id=:productId;";

        HashMap<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        namedParameterJdbcTemplate.update(sql, map);

        return productId;
    }
}
