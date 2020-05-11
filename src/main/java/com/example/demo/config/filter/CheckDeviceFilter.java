package com.example.demo.config.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  用于判断是手机还是电脑访问的本网站
 */
@Component
@WebFilter(urlPatterns = {"/*"},filterName = "checkDeviceFilter")
public class CheckDeviceFilter implements Filter {
    private static final String[] MOBILE_SPECIFIC_SUBSTRING = {"Safari","Chrome","iPad", "iPhone", "Android", "MIDP", "Opera Mobi", "Opera Mini", "BlackBerry", "HP iPAQ", "IEMobile", "MSIEMobile", "Windows Phone", "HTC", "LG", "MOT", "Nokia", "Symbian", "Fennec", "Maemo", "Tear", "Midori", "armv", "Windows CE", "WindowsCE", "Smartphone", "webOS", "Palm", "Sagem", "Samsung", "SGH", "SonyEricsson", "MMP", "UCWEB" };
    private static final Logger log = LoggerFactory.getLogger(com.example.demo.config.filter.CheckDeviceFilter.class);

    public void init(FilterConfig config) throws ServletException {

    }
    public void doFilter(ServletRequest serletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) serletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        // 获取访问的浏览器UA字段
        String UA = ((HttpServletRequest) request).getHeader("User-Agent");
        boolean isMobile = false;

        for (String mobile : MOBILE_SPECIFIC_SUBSTRING) {
            if (UA.contains(mobile) || UA.contains(mobile.toUpperCase()) || UA.contains(mobile.toLowerCase())) {
                log.info(mobile);
                isMobile = true;
            }
        }

        log.info("当前访问的UA字符串为:"+UA);
        log.info("当前请求的路径为:"+request.getRequestURI());

        //用手机访问 并且访问路径不是手机地址
        if(isMobile){
            log.info("当前用手机或电脑访问");
        }else if(!isMobile){
            log.info("爬虫访问");
        }
        filterChain.doFilter(serletRequest, servletResponse);
    }
    @Override
    public void destroy() {

    }
}
