package com.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Administrator
 * @date 2021/12/29 0029
 */
@Controller
public class MyController {
    @GetMapping({"/user/add","/user/update"})
    public String checkUser(){
        System.out.println("执行成功");
        return "index";
    }

    @RequestMapping("/login")
    public String login(String username, String password, Model model){
        Subject subject= SecurityUtils.getSubject();
        UsernamePasswordToken token=new UsernamePasswordToken(username,password);
        try{
            subject.login(token);
            return "index";
        }catch (UnknownAccountException e){
            model.addAttribute("msg","用户名错误");
        }catch (IncorrectCredentialsException e){
            model.addAttribute("msg","密码错误");
        }

        return "login";
    }

    @RequiresRoles(value={"admin","user"})//用来判断角色  同时具有 admin user
    @RequiresPermissions("user:update:01") //用来判断权限字符串
    @RequestMapping("save")
    public String save(){
        System.out.println("进入方法");
        return "redirect:/index.jsp";
    }


}
