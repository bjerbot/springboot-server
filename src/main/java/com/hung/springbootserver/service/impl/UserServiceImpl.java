package com.hung.springbootserver.service.impl;

import com.hung.springbootserver.dao.UserDao;
import com.hung.springbootserver.dto.UserRegisterRequest;
import com.hung.springbootserver.model.User;
import com.hung.springbootserver.service.UserService;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserDao userDao;

    @Override
    public User createUser(UserRegisterRequest userRegisterRequest) {
        //檢查已註冊的email
        User registeredUser = userDao.queryUserByEmail(userRegisterRequest.getEmail());
        if(registeredUser != null){
            log.warn("電子郵件: {}，已被註冊", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        //創建帳號
        Integer UserId = userDao.createUser(userRegisterRequest);

        User newUser = userDao.queryUserById(UserId);

        return newUser;
    }
}
