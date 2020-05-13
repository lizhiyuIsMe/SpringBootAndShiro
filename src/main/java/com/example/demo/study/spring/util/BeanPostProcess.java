package com.example.demo.study.spring.util;

import org.springframework.beans.BeansException;
//自己定义的后置处理器接口
public interface BeanPostProcess {
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
