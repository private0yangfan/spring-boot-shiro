package com.shiro.config;

import com.shiro.pojo.User;
import com.shiro.service.impl.UserServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

/**
 * @author Administrator
 * @date 2021/12/29 0029
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    UserServiceImpl userService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了授权");
        // 硬编码，需要从db中获取用户对应的权限
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        info.addStringPermission("user:add");

        // 拿到user对象
        Subject subject=SecurityUtils.getSubject();
        User currentUser=(User)subject.getPrincipal();
        // 设置当前用户的权限
        info.addStringPermission(currentUser.getPerm());

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行了认证");
        // 用户名，密码  数据中取

//        String name="root";
//        String password="123456";

        UsernamePasswordToken token=(UsernamePasswordToken)authenticationToken;
        // 从数据库获取  用户表
        User user=userService.queryUserByName(token.getUsername());
        if (!token.getUsername().equals(user.getName())){
            return null;
        }
        if(!ObjectUtils.isEmpty(user)){
            return new SimpleAuthenticationInfo(user.getName(),user.getPwd(),
                    new MyByteSource(user.getPerm()),this.getName());
        }

        return new SimpleAuthenticationInfo("", user.getPwd(), "");
    }
}
