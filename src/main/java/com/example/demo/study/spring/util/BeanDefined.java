package com.example.demo.study.spring.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanDefined {
     //id
     private String beadId;
     //class 路径
     private String beanClassPath;
     //是否单例
     private String scope="singleton";
     //是否是factoryBean
     private boolean isFactoryBean=false;
     //要注入的属性
     //第一个参数是属性名  第二个参数是值
     private Map<String,String> property=new ConcurrentHashMap<String, String>();


    public String getBeadId() {
        return beadId;
    }

    public void setBeadId(String beadId) {
        this.beadId = beadId;
    }

    public String getBeanClassPath() {
        return beanClassPath;
    }

    public void setBeanClassPath(String beanClassPath) {
        this.beanClassPath = beanClassPath;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean getIsFactoryBean() {
        return this.isFactoryBean;
    }
    public void setIsFactoryBean(boolean flag) {
        this.isFactoryBean=flag;
    }

    public boolean isFactoryBean() {
        return isFactoryBean;
    }

    public void setFactoryBean(boolean factoryBean) {
        isFactoryBean = factoryBean;
    }

    public Map<String, String> getProperty() {
        return property;
    }

    public void setProperty(Map<String, String> property) {
        this.property = property;
    }
}
