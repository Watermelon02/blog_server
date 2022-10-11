package com.blog.controller;

import com.blog.domain.User;
import com.blog.domain.response.Result;
import com.blog.service.UserService;
import com.blog.util.EmailUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/user")
@CrossOrigin(originPatterns = "*",allowCredentials="true",allowedHeaders = "",methods = {})
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailUtil emailUtil;

    @RequiresGuest
    @GetMapping("/register")
    public Boolean register(@Valid @Email @RequestParam String email) {
        if (!userService.isEmailExist(email)) {
            emailUtil.sendMail("西瓜", email, "注册", "content");
            return true;
        } else return false;
    }

    @RequiresGuest
    @GetMapping("/login")
    public Result<User> login(@RequestParam String email, @RequestParam String password,@RequestParam boolean rememberMe) {
        UsernamePasswordToken token = new UsernamePasswordToken(email, password,rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return new Result<>(200, 1L, (User) SecurityUtils.getSubject().getPrincipal());
        } catch (UnknownAccountException unknownAccountException) {
            return new Result<User>(404, 0L, null);
        }
    }
}
