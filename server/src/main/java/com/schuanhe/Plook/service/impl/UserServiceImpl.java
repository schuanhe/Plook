package com.schuanhe.Plook.service.impl;


import com.schuanhe.Plook.entity.User;
import com.schuanhe.Plook.service.UserService;
import com.schuanhe.Plook.utils.CurPool;
import com.schuanhe.Plook.utils.FixedStringGenerator;
import org.springframework.stereotype.Service;



@Service
public class UserServiceImpl implements UserService {


    @Override
    public User Login(User user) throws Exception{
        // 判断是否存在
        String uid = FixedStringGenerator.generateFixedString(user.getUsername());
        user.setId(uid);
        User user1 = CurPool.userMap.get(user.getId());
        if (user1 == null) {
            throw new Exception("用户不存在");
        }
        // 判断密码是否正确
        if (!user1.getPassword().equals(user.getPassword())) {
            throw new Exception("密码错误");
        }
        return user1;
    }

    @Override
    public User Register(User user) throws Exception{
        String uid = FixedStringGenerator.generateFixedString(user.getUsername());
        user.setId(uid);
        // 判断是否存在
        User user1 = CurPool.userMap.get(uid);
        if (user1 != null) {
            throw new Exception("用户已存在");
        }
        // 判断密码是否正确
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new Exception("密码不能为空");
        }
        // 判断用户名是否正确
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new Exception("用户名不能为空");
        }
        // 判断qq是否正确
        if (user.getQq() == null || user.getQq().isEmpty()) {
            throw new Exception("qq不能为空");
        }
        // 添加到用户池
        CurPool.userMap.put(user.getId(), user);
        return user;
    }


}
