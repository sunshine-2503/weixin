package com.shaoming.user.service;

import com.shaoming.user.model.User;

import java.util.List;

/**
 * Created by jerry on 17-10-11.
 */
public interface UserService {

    /**
     * 查询所有用户信息
     * @return
     */
    List<User> queryAllUsers();

}