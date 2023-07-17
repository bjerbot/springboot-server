package com.hung.springbootserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hung.springbootserver.constant.ProductCategory;
import com.hung.springbootserver.dto.ProductRequest;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    // 查詢單一商品
    @Test
    public void queryProduct_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products/{productId}", 1);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.productName", equalTo("蘋果（澳洲）")))
                .andExpect(jsonPath("$.category", equalTo("FOOD")))
                .andExpect(jsonPath("$.imageUrl", notNullValue()))
                .andExpect(jsonPath("$.price", notNullValue()))
                .andExpect(jsonPath("$.stock", notNullValue()))
                .andExpect(jsonPath("$.description", notNullValue()))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));
    }

    @Test
    public void queryProduct_notFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products/{productId}", 100);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    //新增商品
    @Transactional
    @Test
    public void createProduct_success() throws Exception{
        ProductRequest productRequest =new ProductRequest();
        productRequest.setProductName("Iphone 6s");
        productRequest.setCategory(ProductCategory.SMART_PHONE);
        productRequest.setImageUrl("https://test.com");
        productRequest.setPrice(6000);
        productRequest.setStock(3);

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products")
//                .header("Content-Type", "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName", equalTo("Iphone 6s")))
                .andExpect(jsonPath("$.category", equalTo("SMART_PHONE")))
                .andExpect(jsonPath("$.imageUrl", equalTo("https://test.com")))
                .andExpect(jsonPath("$.price", equalTo(6000)))
                .andExpect(jsonPath("$.stock", equalTo(3)))
                .andExpect(jsonPath("$.description", nullValue()))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));
    }

    @Transactional
    @Test
    public void createProduct_illegalArgument() throws Exception{
        ProductRequest productRequest =new ProductRequest();
        productRequest.setProductName("Iphone 6s");
        productRequest.setCategory(ProductCategory.SMART_PHONE);
        productRequest.setImageUrl("https://test.com");
//        讓price的值為null，去測試請求參數的驗證是否生效，也模擬發生bad request的情況
//        productRequest.setPrice(6000);
        productRequest.setStock(3);

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products")
//                .header("Content-Type", "application/json")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    //更新商品
    @Transactional
    @Test
    public void updateProduct_success() throws Exception {
        ProductRequest productRequest =new ProductRequest();
        productRequest.setProductName("Iphone 6s");
        productRequest.setCategory(ProductCategory.SMART_PHONE);
        productRequest.setImageUrl("https://test.com");
        productRequest.setPrice(6000);
        productRequest.setStock(3);

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/products/{productId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", equalTo("Iphone 6s")))
                .andExpect(jsonPath("$.category", equalTo("SMART_PHONE")))
                .andExpect(jsonPath("$.imageUrl", equalTo("https://test.com")))
                .andExpect(jsonPath("$.price", equalTo(6000)))
                .andExpect(jsonPath("$.stock", equalTo(3)))
                .andExpect(jsonPath("$.description", nullValue()))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));
    }

    @Transactional
    @Test
    public void updateProduct_illegalArgument() throws Exception {
        ProductRequest productRequest =new ProductRequest();
        productRequest.setDescription("description");

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/products/{productId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Transactional
    @Test
    public void updateProduct_notFound() throws Exception {
        ProductRequest productRequest =new ProductRequest();
        productRequest.setProductName("Iphone 6s");
        productRequest.setCategory(ProductCategory.SMART_PHONE);
        productRequest.setImageUrl("https://test.com");
        productRequest.setPrice(6000);
        productRequest.setStock(3);

        String json = objectMapper.writeValueAsString(productRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/products/{productId}", 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound());
    }

    //刪除商品
    @Transactional
    @Test
    public void deleteProduct_success() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/products/{productId}", 4);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Transactional
    @Test
    public void deleteProduct_notExitProduct() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/products/{productId}", 999);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    // 查詢商品列表
    @Test
    public void queryProducts_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.limit", equalTo(4)))
                .andExpect(jsonPath("$.offset", equalTo(0)))
                .andExpect(jsonPath("$.total", equalTo(7)))
                .andExpect(jsonPath("$.result", hasSize(4)))
                .andExpect(jsonPath("$.result[0].productName", equalTo("Benz")))
                .andExpect(jsonPath("$.result[0].category", equalTo("CAR")))
                .andExpect(jsonPath("$.result[0].imageUrl", equalTo("https://cdn.pixabay.com/photo/2017/03/27/14/56/auto-2179220_1280.jpg")))
                .andExpect(jsonPath("$.result[0].price", equalTo(600000)))
                .andExpect(jsonPath("$.result[0].stock", equalTo(2)))
                .andExpect(jsonPath("$.result[0].description", nullValue()));
    }

    @Test
    public void queryProducts_filtering() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products?category=CAR&search=Tesla");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.limit", equalTo(4)))
                .andExpect(jsonPath("$.offset", equalTo(0)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.result", hasSize(1)))
                .andExpect(jsonPath("$.result[0].productName", equalTo("Tesla")))
                .andExpect(jsonPath("$.result[0].category", equalTo("CAR")))
                .andExpect(jsonPath("$.result[0].imageUrl", equalTo("https://cdn.pixabay.com/photo/2021/01/15/16/49/tesla-5919764_1280.jpg")))
                .andExpect(jsonPath("$.result[0].price", equalTo(450000)))
                .andExpect(jsonPath("$.result[0].stock", equalTo(5)))
                .andExpect(jsonPath("$.result[0].description", equalTo("世界最暢銷的充電式汽車")));
    }

    @Test
    public void queryProducts_sorting() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products?orderBy=product_id&sort=asc");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.limit", equalTo(4)))
                .andExpect(jsonPath("$.offset", equalTo(0)))
                .andExpect(jsonPath("$.total", equalTo(7)))
                .andExpect(jsonPath("$.result", hasSize(4)))
                .andExpect(jsonPath("$.result[2].productName", equalTo("好吃又鮮甜的蘋果橘子")))
                .andExpect(jsonPath("$.result[2].category", equalTo("FOOD")))
                .andExpect(jsonPath("$.result[2].imageUrl", equalTo("https://cdn.pixabay.com/photo/2021/07/30/04/17/orange-6508617_1280.jpg")))
                .andExpect(jsonPath("$.result[2].price", equalTo(10)))
                .andExpect(jsonPath("$.result[2].stock", equalTo(50)))
                .andExpect(jsonPath("$.result[2].description", nullValue()));
    }

    @Test
    public void queryProducts_pagination() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/products?limit=3&offset=2&orderBy=product_id&sort=asc");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.limit", equalTo(3)))
                .andExpect(jsonPath("$.offset", equalTo(2)))
                .andExpect(jsonPath("$.total", equalTo(7)))
                .andExpect(jsonPath("$.result", hasSize(3)))
                .andExpect(jsonPath("$.result[2].productName", equalTo("BMW")))
                .andExpect(jsonPath("$.result[2].category", equalTo("CAR")))
                .andExpect(jsonPath("$.result[2].imageUrl", equalTo("https://cdn.pixabay.com/photo/2018/02/21/03/15/bmw-m4-3169357_1280.jpg")))
                .andExpect(jsonPath("$.result[2].price", equalTo(500000)))
                .andExpect(jsonPath("$.result[2].stock", equalTo(3)))
                .andExpect(jsonPath("$.result[2].description", equalTo("渦輪增壓，直列4缸，DOHC雙凸輪軸，16氣門")));
    }
}
