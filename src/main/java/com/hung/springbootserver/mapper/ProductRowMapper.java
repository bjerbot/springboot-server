package com.hung.springbootserver.mapper;

import com.hung.springbootserver.constant.ProductCategory;
import com.hung.springbootserver.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ProductRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getInt("product_id"));
        product.setProductName(rs.getString("product_name"));

        ProductCategory category = ProductCategory.valueOf(rs.getString("category"));
        product.setCategory(category );

        product.setImageUrl(rs.getString("image_url"));
        product.setPrice(rs.getInt("price"));
        product.setStock(rs.getInt("stock"));
        product.setDescription(rs.getString("description"));

        LocalDateTime createDate = rs.getTimestamp("created_date").toLocalDateTime();
        LocalDateTime lastModifiedDate = rs.getTimestamp("last_modified_date").toLocalDateTime();

        product.setCreatedDate(createDate);
        product.setLastModifiedDate(lastModifiedDate);

        return product;
    }
}
