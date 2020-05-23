package com.example.demo.study.spring.aop;

import com.example.demo.study.spring.util.BeanPostProcess;
import org.springframework.beans.BeansException;

public class AopBeanPostProcess implements BeanPostProcess,Aop {

    //对要增强的bean进行代理
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        System.out.println("--------");
        return bean;
    }
}
