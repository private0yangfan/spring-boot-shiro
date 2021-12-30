package com.shiro.mapper;


import com.shiro.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @date 2021/12/30 0030
 */
@Repository
@Mapper
public interface UserMapper {

    public User queryUserByName(String name);
}
