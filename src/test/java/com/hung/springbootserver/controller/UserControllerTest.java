package com.hung.springbootserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hung.springbootserver.dao.UserDao;
import com.hung.springbootserver.dto.UserLoginRequest;
import com.hung.springbootserver.dto.UserRegisterRequest;
import com.hung.springbootserver.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.DigestUtils;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    //已被spring container自動配置，所以可以直接注入bean
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDao userDao;

    //註冊帳號的單元測試
    @Test
    public void register_success() throws Exception {
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("a123@email.com");
        userRegisterRequest.setPassword("a123");

        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.userId", notNullValue()))
                .andExpect(jsonPath("$.email", equalTo("a123@email.com")))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));

        //檢查資料庫中的密碼使否為Hash Value
        User user = userDao.queryUserByEmail(userRegisterRequest.getEmail());
        String expectPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        assertEquals(expectPassword, user.getPassword());
    }

    @Test
    public void register_emailAlreadyExist() throws Exception {
        //先註冊新帳號
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("b123@email.com");
        userRegisterRequest.setPassword("b123");

        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201));

        //用同樣的email再次註冊
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    public void register_invalidEmailFormat() throws Exception {
        //使用錯誤的email格式，註冊帳號
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setEmail("invalid_email_format");
        userRegisterRequest.setPassword("b123");

        String json = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    public void login_success() throws Exception{
        //先註冊新帳號
        UserRegisterRequest userRegisterRequest =new UserRegisterRequest();
        userRegisterRequest.setEmail("h123@email.com");
        userRegisterRequest.setPassword("h123");

        String registerJson = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder registerRequestBuilder = MockMvcRequestBuilders
                .post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson);

        mockMvc.perform(registerRequestBuilder)
                .andExpect(status().is(201));

        //測試登入帳號
        UserLoginRequest userLoginRequest =new UserLoginRequest();
        userLoginRequest.setEmail(userRegisterRequest.getEmail());
        userLoginRequest.setPassword(userRegisterRequest.getPassword());

        String loginJson = objectMapper.writeValueAsString(userLoginRequest);

        RequestBuilder loginRequestBuilder = MockMvcRequestBuilders
                .post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson);

        mockMvc.perform(loginRequestBuilder)
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.userId", notNullValue()))
                .andExpect(jsonPath("$.email", equalTo("h123@email.com")))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));
    }

    @Test
    public void login_emailNotExist() throws Exception{
        //測試使用不存在的email登入帳號
        UserLoginRequest userLoginRequest =new UserLoginRequest();
        userLoginRequest.setEmail("not_exist_email@gmail.com");
        userLoginRequest.setPassword("a123");

        String loginJson = objectMapper.writeValueAsString(userLoginRequest);

        RequestBuilder loginRequestBuilder = MockMvcRequestBuilders
                .post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson);

        mockMvc.perform(loginRequestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    public void login_wrongPassword() throws Exception{
        //先註冊新帳號
        UserRegisterRequest userRegisterRequest =new UserRegisterRequest();
        userRegisterRequest.setEmail("test@email.com");
        userRegisterRequest.setPassword("a123");

        String registerJson = objectMapper.writeValueAsString(userRegisterRequest);

        RequestBuilder registerRequestBuilder = MockMvcRequestBuilders
                .post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerJson);

        mockMvc.perform(registerRequestBuilder)
                .andExpect(status().is(201));

        //測試使用錯誤的password登入帳號
        UserLoginRequest userLoginRequest =new UserLoginRequest();
        userLoginRequest.setEmail(userLoginRequest.getEmail());
        userLoginRequest.setPassword("wrong_password");

        String loginJson = objectMapper.writeValueAsString(userLoginRequest);

        RequestBuilder loginRequestBuilder = MockMvcRequestBuilders
                .post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson);

        mockMvc.perform(loginRequestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    public void login_invalidEmailFormat() throws Exception{
        //使用錯誤的email格式，註冊帳號
        UserLoginRequest userLoginRequest =new UserLoginRequest();
        userLoginRequest.setEmail("invalid_email_format");
        userLoginRequest.setPassword("a123");

        String loginJson = objectMapper.writeValueAsString(userLoginRequest);

        RequestBuilder loginRequestBuilder = MockMvcRequestBuilders
                .post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson);

        mockMvc.perform(loginRequestBuilder)
                .andExpect(status().is(400));
    }

}