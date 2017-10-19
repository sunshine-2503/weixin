package com.shaoming.user.service.impl;

import com.shaoming.user.mapper.UserMapper;
import com.shaoming.user.model.User;
import com.shaoming.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jerry on 17-10-11.
 */
@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> queryAllUsers() {
        return userMapper.queryAllUsers();
    }
}
