package com.shiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.shiro.redis.RedisCacheManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Administrator
 * @date 2021/12/29 0029
 */
@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean=new ShiroFilterFactoryBean();
        bean.setSecurityManager(defaultWebSecurityManager);

        // 添加shiro内置过滤器
        /*
            anon:无需认证
            authc: 必须认证
            user: 必须拥有 记住我 功能才能用
            perms: 拥有对某个资源的权限才能访问
            role: 拥有某个角色权限才能访问
         */
        Map<String, String> filterChainDefinitionMap=new LinkedHashMap<String, String>();
        // 授权，正常情况下，没有授权会跳转到未授权页面
        filterChainDefinitionMap.put("/user/add","perms[user:add]");
        filterChainDefinitionMap.put("/login","anon");
        filterChainDefinitionMap.put("/user/update","authc");
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        // 设置登录的请求
        bean.setLoginUrl("/toLogin");
        // 未授权页面
        bean.setUnauthorizedUrl("/noauth");
        return bean;
    }
    @Bean(name="securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        return securityManager;
    }
    @Bean
    public UserRealm userRealm(){
        UserRealm userRealm=new UserRealm();
        //设置hashed凭证匹配器
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //设置md5加密
        credentialsMatcher.setHashAlgorithmName("md5");
        //设置散列次数
        credentialsMatcher.setHashIterations(1024);
        userRealm.setCredentialsMatcher(credentialsMatcher);

        //开启缓存管理
        userRealm.setCacheManager(new RedisCacheManager());
        userRealm.setCachingEnabled(true);//开启全局缓存
        userRealm.setAuthenticationCachingEnabled(true);//认证认证缓存
        userRealm.setAuthenticationCacheName("authenticationCache");
        userRealm.setAuthorizationCachingEnabled(true);//开启授权缓存
        userRealm.setAuthorizationCacheName("authorizationCache");
        return userRealm;
    }

    // 整合shiroDialect:用来整合shiro thymeleaf
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }
}
