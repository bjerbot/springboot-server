package com.hung.springbootserver.controller;

import com.hung.springbootserver.dto.TokenRequest;
import com.hung.springbootserver.dto.UserLoginRequest;
import com.hung.springbootserver.dto.UserRegisterRequest;
import com.hung.springbootserver.model.AuthenticationResponse;
import com.hung.springbootserver.model.User;
import com.hung.springbootserver.service.UserService;
import com.hung.springbootserver.security.JwtTokenUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PostMapping("/users/register")
    public ResponseEntity<User> Register(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        User newUser = userService.createUser(userRegisterRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/users/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid UserLoginRequest userLoginRequest){
        User user = userService.queryUserByEmail(userLoginRequest);

        String token = jwtTokenUtil.generateToken(user.getEmail());

        AuthenticationResponse authenticationResponse =new AuthenticationResponse();
        authenticationResponse.setUser(user);
        authenticationResponse.setToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
    }

    @PostMapping("/validate_login_status")
    public ResponseEntity validateLoginStatus(@RequestHeader("Authorization") String authorization, @RequestBody @Valid TokenRequest tokenRequest){

        String jwtToken = authorization.replace("Bearer ", "");
//        System.out.println(jwtToken);

        boolean isLogin = jwtTokenUtil.validateToken(jwtToken, tokenRequest.getEmail());

        if(isLogin){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
