package com.example.demo.config.filter;

import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

/**
 * 对request对象进行包装达到防止xss攻击
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public Cookie[] getCookies() {
        Cookie[] cookies = super.getCookies();
        if (cookies !=null) {
            for(Cookie cookie:cookies){
                String value = cookie.getValue();
                cookie.setValue(StringEscapeUtils.escapeHtml4(value));
            }
            return cookies;
        }else{
            return null;
        }
    }

    @Override
    public Enumeration<String> getHeaders(String name){
         throw  new RuntimeException("这里不想写了,别用这里代码");
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = super.getParameterMap();
        if(parameterMap !=null){
            for(Map.Entry<String, String[]> entry: parameterMap.entrySet()){
                String[] values = entry.getValue();
                if(values != null && values.length>0){
                    for(int i=0;i<values.length;i++){
                        values[i]=StringEscapeUtils.escapeHtml4(values[i]);
                    }
                    entry.setValue(values);
                }
            }
            return parameterMap;
        }else{
            return null;
        }
    }


    @Override
    public String getHeader(String name) {
        return StringEscapeUtils.escapeHtml4(super.getHeader(name));
    }

    @Override
    public String getParameter(String name) {
        return StringEscapeUtils.escapeHtml4(super.getParameter(name));
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] =  StringEscapeUtils.escapeHtml4(values[i]);
        }
        return encodedValues;
    }

}
