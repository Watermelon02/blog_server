package com.blog.shiro;

import com.blog.domain.User;
import com.blog.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;

public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new  SimpleAuthorizationInfo();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        System.out.println(user.getRole());
        HashSet<String> roleSet = new HashSet<>();
        roleSet.add(user.getRole());
        authorizationInfo.setRoles(roleSet);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String email = (String) authenticationToken.getPrincipal();
        String password = new String((char[]) authenticationToken.getCredentials());
        User user = userService.login(email, password);
        if (user == null) {
            throw new UnknownAccountException("用户名或密码错误！");
        }
        return new SimpleAuthenticationInfo(user, password, getName());
    }
}
