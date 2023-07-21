package com.hung.springbootserver.controller;

import com.hung.springbootserver.dto.UserLoginRequest;
import com.hung.springbootserver.dto.UserRegisterRequest;
import com.hung.springbootserver.model.User;
import com.hung.springbootserver.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/users/register")
    public ResponseEntity<User> Register(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        User newUser = userService.createUser(userRegisterRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/users/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserLoginRequest userLoginRequest){
        User user = userService.queryUserByEmail(userLoginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
