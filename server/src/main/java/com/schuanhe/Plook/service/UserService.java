package com.schuanhe.Plook.service;

import com.schuanhe.Plook.entity.User;

import java.util.List;

public interface UserService {
    /**
     * 登录用户
     * @param user 用户数据
     * @return 成功登录用户数据
     */
    User Login(User user);

    /**
     * 注册
     * @param user 用户数据
     * @return 成功则是包括id的用户数据，失败则没用id
     */
    User Register(User user);

    /**
     * 查询使用用户数据
     * @return 用户数据
     */
    List<User> queryUserList();
}
