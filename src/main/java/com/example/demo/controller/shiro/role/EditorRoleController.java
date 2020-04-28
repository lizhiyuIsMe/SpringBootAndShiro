package com.example.demo.controller.shiro.role;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/video")
public class EditorRoleController {

    @RequestMapping("/update")
    public String  query(){
         return "有指定权限才能访问这个页面";
    }
}
