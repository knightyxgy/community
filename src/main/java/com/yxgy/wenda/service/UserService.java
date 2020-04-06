package com.yxgy.wenda.service;

import com.yxgy.wenda.dao.UserMapper;
import com.yxgy.wenda.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

}
