package com.example.demo.controller.shiro.pub;

import com.example.demo.dto.QueryUser;
import com.example.demo.service.UserService;
import com.example.demo.util.JsonData;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

//没有登录也能访问
@RestController
@RequestMapping("/pub")
public class PublicController {
    @Autowired
    private UserService userService;

//    请求json格式
//    {
//        "userName":1,
//        "password":1,
//        "readMe":1
//    }
    @PostMapping("/login")
    public JsonData  login(@RequestBody QueryUser queryUser, HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> info = new HashMap<>();

        String userId=queryUser.getUserId();
        String password=queryUser.getPassword();
        try{
            //根据用户输入的信息创建token
            AuthenticationToken authenticationToken=new UsernamePasswordToken(userId,password);
            //获得用户也可以是机器对象
            Subject subject = SecurityUtils.getSubject();
            //调用securityManager 绑定的realm 中的认证方法
            subject.login(authenticationToken);

            info.put("msg","登录成功");
            info.put("session_id", subject.getSession().getId());

            return  JsonData.buildSuccess(info);
        }catch (Exception e){
            e.printStackTrace();
            return JsonData.buildError("账号或者密码错误");
        }
    }

    @RequestMapping("/need_login")
    public String  needLogin(){
         return "你没登录,去登陆吧";
    }

    @RequestMapping("/not_permit")
    public String  notPermit(){
        return "你没权限访问该资源";
    }
}
