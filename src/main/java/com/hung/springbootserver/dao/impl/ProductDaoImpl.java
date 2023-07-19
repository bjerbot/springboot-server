package com.hung.springbootserver.dao.impl;

import com.hung.springbootserver.dao.ProductDao;
import com.hung.springbootserver.dto.ProductQueryParams;
import com.hung.springbootserver.dto.ProductRequest;
import com.hung.springbootserver.model.Product;
import com.hung.springbootserver.mapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    //查詢指定category的商品Dao層
    public List<Product> queryProducts(ProductQueryParams productQueryParams){
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, " +
                "created_date, last_modified_date FROM product  WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        //查詢條件
        sql = addSqlQueryConditions(sql, map, productQueryParams);

        //排序
        sql = sql + " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();

        //分頁
        sql = sql + " LIMIT :limit OFFSET :offset";
        map.put("limit", productQueryParams.getLimit());
        map.put("offset", productQueryParams.getOffset());

        return namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
    }

    //查詢數量
    public Integer countProduct(ProductQueryParams productQueryParams){
        String sql = "SELECT COUNT(*) FROM product WHERE 1=1";

        Map<String, Object> map =new HashMap();

        //查詢條件
        sql = addSqlQueryConditions(sql, map, productQueryParams);

        return namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
    }

    //查詢商品Dao層
    @Override
    public Product queryProductById(Integer productId) {
        String sql = "SELECT product_id, product_name, category, image_url, " +
                "price, stock, description, created_date, last_modified_date\n" +
                "FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        if(productList.size() > 0) {
            return productList.get(0);
        }
        else{
            return null;
        }
    }

    //新增商品Dao層
    public Integer createProduct(ProductRequest productRequest){
        String sql = "INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date) " +
                "VALUES (:productName, :category, :imageUrl, :price, :stock, :description, :createdDate, :lastModifiedDate)";

        String create = "create";
        Map<String, Object> map = setProductRequest(create, null, productRequest);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        int productId = keyHolder.getKey().intValue();
        return productId;
    }

    //修改商品Dao層
    @Override
    public Integer updateProduct(Integer productId,  ProductRequest productRequest){
        String sql = "UPDATE product SET product_name=:productName, category=:category, image_url=:imageUrl, " +
                "price=:price, stock=:stock, description=:description, " +
                "last_modified_date=:lastModifiedDate WHERE product_id=:productId;";

        String update = "update";
        Map<String, Object> map = setProductRequest(update, productId, productRequest);

        namedParameterJdbcTemplate.update(sql, map);
        return productId;
    }

    //刪除商品Dao層
    @Override
    public void deleteProductById(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id=:productId;";

        HashMap<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        namedParameterJdbcTemplate.update(sql, map);
    }

    private Map<String, Object> setProductRequest(String action, Integer productId,  ProductRequest productRequest){
        HashMap<String, Object> map = new HashMap<>();
        if(action.equals("update")) {
            map.put("productId", productId);
        }
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Instant nowDateTime = Instant.now();
        if(action.equals("create")) {
            map.put("createdDate", nowDateTime);
        }
        map.put("lastModifiedDate", nowDateTime);
        return map;
    }
    
    private String addSqlQueryConditions(String sql, Map<String, Object> map, ProductQueryParams productQueryParams){
        if(productQueryParams.getCategory() != null){
            sql = sql + " AND category = :category";
            map.put("category", productQueryParams.getCategory());
        }
        if(productQueryParams.getSearch() != null){
            sql = sql + " AND product_name LIKE :search";
            map.put("search", "%" + productQueryParams.getSearch()+ "%");
        }
//        System.out.println(sql);
        return sql;
    }
}
