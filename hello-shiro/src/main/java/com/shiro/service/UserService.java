package com.shiro.service;

import com.shiro.mapper.UserMapper;
import com.shiro.pojo.User;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @date 2021/12/30 0030
 */
public interface UserService  {

    public User queryUserByName(String name);
}
