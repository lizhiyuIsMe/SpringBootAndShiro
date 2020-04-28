package com.example.demo.config.shiro.realm;

import com.example.demo.domain.Permission;
import com.example.demo.domain.Role;
import com.example.demo.domain.Users;
import com.example.demo.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 自己定义的relam
 */
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    UserService userServiceImpl;

    //进行认证,一般在登录的时候进行认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        SimpleAuthenticationInfo simpleAuthenticationInfo = null;
        //用户输入的信息 用户名(唯一标识)
        String userid = (String) authenticationToken.getPrincipal();
        if (!StringUtils.isEmpty(userid)) {
            //根据用户id 查询密码
            Users user = userServiceImpl.queryUserByUserId(userid);
            String password=user.getPassword();
            if(user !=null && !StringUtils.isEmpty(password)){
                //simpleAuthenticationInfo = new SimpleAuthenticationInfo(userid,password,this.getClass().getName());
                //这里有一个坑,当使用redis缓存session时,会报这样一个错,说必须要设置一个唯一值给redis,这样他才能存sessionid
//                {
//                  "timestamp": "2020-04-28T09:36:16.821+0000",
//                   "status": 500,
//                   "error": "Internal Server Error",
//                   "message": "class java.lang.String must has getter for field: authCacheKey or id\nWe need a field to identify this Cache Object in Redis. So you need to defined an id field which you can get unique id to identify this principal. For example, if you use UserInfo as Principal class, the id field maybe userId, userName, email, etc. For example, getUserId(), getUserName(), getEmail(), etc.\nDefault value is authCacheKey or id, that means your principal object has a method called \"getAuthCacheKey()\" or \"getId()\"",
//                   "path": "/admin/query"
//                }
                //当使用如下依赖时
//            <dependency>
//                <groupId>org.crazycake</groupId>
//                <artifactId>shiro-redis</artifactId>
//                 <version>3.1.0</version>
//             </dependency>
                //解决方法是第一参数要传入一个user对象,而不是userid字符串(user对象中要有一个id属性)
                simpleAuthenticationInfo = new SimpleAuthenticationInfo(user,password,this.getClass().getName());
            }
        }
        //返回对象后 自己shiro会进行校验
        return simpleAuthenticationInfo;
    }


    /**
     * 授权
     * 根据传入的当前用户信息,获得他的所有角色和权限 封装到AuthorizationInfo 对象返回
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //用户信息
        //由于认证时候存的是Users对象,取得时候也是Users对象
        Users userObject= (Users) principalCollection.getPrimaryPrincipal();
        Users users = userServiceImpl.queryUserByUserId(userObject.getId());

        //存储当前用户的角色信息
        Set<String> roleSet=new HashSet<String>();
        //存储当前用户的权限信息
        Set<String> PermSet=new HashSet<String>();

        List<Role> roleList = users.getRoleList();
        if (roleList != null) {
            for (Role role : roleList) {
                //本有含有的角色名
                roleSet.add(role.getName());
                List<Permission> permissionList = role.getPermissionList();
                if (permissionList != null) {
                    for (Permission permission : permissionList) {
                        //可以访问资源的路径
                        PermSet.add(permission.getName());
                    }
                }
            }
        }

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //设置本人有的的角色
        simpleAuthorizationInfo.setRoles(roleSet);
        //设置本人含有的权限
        simpleAuthorizationInfo.setStringPermissions(PermSet);
        return simpleAuthorizationInfo;
    }


}
