package com.tl.commerce.service.impl;

import com.tl.commerce.domain.User;
import com.tl.commerce.mapper.UserMapper;
import com.tl.commerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户逻辑类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;
    @Override
    public User getUserByLoginPhone(String login_phone) {

        return userMapper.getUserByLoginPhone(login_phone);
    }

    @Override
    public int save(User user) {
        return userMapper.save(user);
    }
}
