package com.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.domain.User;
import com.blog.domain.response.Result;
import com.blog.mapper.UserMapper;
import com.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author xigua
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2022-09-27 18:44:37
 */
@Component
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Boolean isEmailExist(String email) {
        User user = userMapper.selectByEmailEquals(email);
        if (user != null) {
            return true;
        } else return false;
    }

    public User login(String email, String password){
        return userMapper.selectByEmailAndPassword(email, password);
    }

    @Override
    public User selectByUserId(Long userId) {
        return userMapper.selectByUserId(userId);
    }
}




