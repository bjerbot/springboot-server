package com.hung.springbootserver.dao.impl;

import com.hung.springbootserver.dao.UserDao;
import com.hung.springbootserver.dto.UserRegisterRequest;
import com.hung.springbootserver.mapper.UserRowMapper;
import com.hung.springbootserver.model.User;
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
public class UserDaoImpl implements UserDao {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {
        String sql = "INSERT INTO `user` (email, password, created_date, last_modified_date) " +
                "VALUES (:email, :password, :createdDate, :lastModifiedDate)";
        Map<String, Object> map = new HashMap<>();
        map.put("email", userRegisterRequest.getEmail());
        map.put("password", userRegisterRequest.getPassword());

        Instant nowDateTime = Instant.now();
        map.put("createdDate", nowDateTime);
        map.put("lastModifiedDate", nowDateTime);

        KeyHolder keyHolder =new  GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int userId = keyHolder.getKey().intValue();

        return userId;
    }

    @Override
    public User queryUserById(Integer userId) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date " +
                "FROM `user` WHERE user_id = :userId";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());
        if(userList.size()>0){
            return userList.get(0);
        }
        else{
            return null;
        }
    }

    @Override
    public User queryUserByEmail(String email) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date " +
                "FROM `user` WHERE email = :email";

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());
        if(userList.size()>0){
            return userList.get(0);
        }
        else{
            return null;
        }
    }
}
