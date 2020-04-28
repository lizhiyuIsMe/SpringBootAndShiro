package com.example.demo.service.impl;

import com.example.demo.dao.UserMapper;
import com.example.demo.domain.Users;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public Users queryUserByUserId(String uiserid) {
        Users user = userMapper.findById(uiserid);
        return user;
    }
}
