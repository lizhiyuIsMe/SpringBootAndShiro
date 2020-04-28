package com.example.demo.config.shiro.filter;

import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

/**
 * 当shiro底层的拦截满足不了需求时,可以自己定义一个拦截器
 * 这个拦截器 标识配置时只要有一个角色满足即可访问(shrio自带的要所有角色都含有才能访问)
 */

public class CustomRolesOrAuthorizationFilter extends AuthorizationFilter {

    public CustomRolesOrAuthorizationFilter() {
    }

    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        //获得用户对象
        Subject subject = this.getSubject(request, response);
        //获得配置的所有的角色
        String[] rolesArray = (String[])((String[])mappedValue);

        //如果没有配置角色直接通过
        if (rolesArray != null && rolesArray.length != 0) {
            //如果配置角色了,有其中一个角色即可通过
            Set<String> roles = CollectionUtils.asSet(rolesArray);
            for(String role:roles){
                if(subject.hasRole(role)){
                    return true;
                }
            }
            return false;
            //return subject.hasAllRoles(roles);
        } else {
            return true;
        }
    }
}