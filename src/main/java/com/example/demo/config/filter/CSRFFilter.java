package com.example.demo.config.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 防盗链和 csrf操作
 *
 */
@Component
@WebFilter(urlPatterns = {"/*"},filterName = "csrfFilter")
public class CSRFFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res= (HttpServletResponse)response;
        // 从 HTTP 头中取得 Referer 值
        String referer = req.getHeader("Referer");
        String allowReferers="";
        String[] refers = allowReferers.split(","); // 可以直接放行的路径，不用校验referer
        boolean flag=true;
        // 如果referer为null的处理（ie windows.open() referer是为null的）
        if (StringUtils.isBlank(referer)) {
        } else {
            for(String str: refers){
                if (referer.trim().contains(str)) {
                    flag=false;
                }
            }
        }

        //测试使用代码
        flag =false;

        if(flag){
            res.sendRedirect("首页");
        }else{
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
