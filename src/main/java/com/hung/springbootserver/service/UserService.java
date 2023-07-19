package com.hung.springbootserver.service;

import com.hung.springbootserver.dto.UserRegisterRequest;
import com.hung.springbootserver.model.User;

public interface UserService {

    User createUser(UserRegisterRequest userRegisterRequest);
}
