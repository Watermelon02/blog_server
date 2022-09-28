package com.blog.controller;

import com.blog.domain.User;
import com.blog.domain.response.Result;
import com.blog.service.UserService;
import com.blog.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailUtil emailUtil;

    @GetMapping("/register")
    public Boolean register(@Valid @Email @RequestParam String email) {
        if (!userService.isEmailExist(email)) {
            emailUtil.sendMail("西瓜",email,"注册","content");
            return true;
        } else return false;
    }

    @PostMapping("/login")
    public Result<User> login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password);
    }
}
