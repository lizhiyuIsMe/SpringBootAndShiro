package com.example.demo.controller.shiro.root;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//有 root 角色才能访问
@RestController
@RequestMapping("/root")
public class AdminRoleController {

    @PostMapping("/query")
    public String  query(){
         return "有admin角色才能看到这个页面";
    }
}
