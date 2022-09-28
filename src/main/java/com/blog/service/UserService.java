package com.blog.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.domain.User;
import com.blog.domain.response.Result;

/**
* @author xigua
* @description 针对表【user】的数据库操作Service
* @createDate 2022-09-27 18:44:37
*/
public interface UserService extends IService<User> {
    Boolean isEmailExist(String email);

    public Result<User> login(String account, String password);
}
