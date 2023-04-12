package com.schuanhe.Plook.mapper;


import com.schuanhe.Plook.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
@Component
public interface UserMapper{
    /**
     * 通过账号密码传参
     * @param user user实体类
     * @return 符合条件的数据
     */
    User queryUserByUp(@RequestParam("user") User user);

    /**
     * 查询所有用户
     * @return 查询所有用户
     */
    List<User> queryUserList();

    /**
     * 增加用户
     * @param user user对象来添加
     * @return 返回增加的条数
     */
    User addUser(@RequestParam("user") User user);

    /**
     * 通过用户名查询数据
     * @param username 用户名
     * @return 用户数据
     */
    List<User> queryUserByUserName(@RequestParam("username") String username);


}
