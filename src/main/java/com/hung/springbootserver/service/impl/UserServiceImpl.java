package com.hung.springbootserver.service.impl;

import com.hung.springbootserver.dao.UserDao;
import com.hung.springbootserver.dto.UserLoginRequest;
import com.hung.springbootserver.dto.UserRegisterRequest;
import com.hung.springbootserver.model.User;
import com.hung.springbootserver.service.UserService;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserDao userDao;

    @Override
    public User createUser(UserRegisterRequest userRegisterRequest) {
        //檢查email是否已有註冊的
        User registeredUser = userDao.queryUserByEmail(userRegisterRequest.getEmail());
        if(registeredUser != null){
            log.warn("電子郵件: {}，已被註冊", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //將密碼轉為MD5雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        //創建帳號
        Integer UserId = userDao.createUser(userRegisterRequest);

        User newUser = userDao.queryUserById(UserId);

        return newUser;
    }

    @Override
    public User queryUserByEmail(UserLoginRequest userLoginRequest) {
        //檢查email是否已有註冊
        User registeredUser = userDao.queryUserByEmail(userLoginRequest.getEmail());
        if(registeredUser == null){
            log.warn("此電子郵件: {}，尚未被註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //將密碼轉為MD5雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        //檢查密碼是否正確
        if(registeredUser.getPassword().equals(hashedPassword)){
            return registeredUser;
        }
        else{
            log.warn("電子郵件或密碼不正確", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
