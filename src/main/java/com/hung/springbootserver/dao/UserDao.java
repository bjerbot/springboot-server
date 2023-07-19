package com.hung.springbootserver.dao;

import com.hung.springbootserver.dto.UserRegisterRequest;
import com.hung.springbootserver.model.User;

public interface UserDao {

    Integer createUser(UserRegisterRequest userRegisterRequest);

    User queryUserById(Integer userId);

    User queryUserByEmail(String email);
}
