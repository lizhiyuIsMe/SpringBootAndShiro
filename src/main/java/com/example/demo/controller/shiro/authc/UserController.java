package com.example.demo.controller.shiro.authc;

import com.example.demo.domain.Users;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

//只有登陆了才能访问这个链接
@RestController
    @RequestMapping("/authc")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/queryUser")
    public Users queryUser(HttpServletRequest request){
        String id = request.getParameter("id");
        Users user = userService.queryUserByUserId(id);
        return user;
    }

    @RequestMapping("/query")
    public String  query(){
        return "登录后才能看到这个页面";
    }

}
