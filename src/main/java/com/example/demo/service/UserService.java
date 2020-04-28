package com.example.demo.service;

import com.example.demo.domain.Users;

public interface UserService {

    //根据用户id 查询用户信息
    Users queryUserByUserId(String uiserid);
}
