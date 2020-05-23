package com.example.demo.study.spring.util;

import com.example.demo.study.spring.service.TestService;
import org.springframework.beans.BeansException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyBeanPostProcess  implements BeanPostProcess ,Common{
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    //通过动态带进行增强
    @Override
    public Object postProcessAfterInitialization(final Object bean, String beanName) throws BeansException {
        Class<?> instanceClass = bean.getClass();

        //使用动态代理对对象进行增强
        if(instanceClass == TestService.class){
            return  Proxy.newProxyInstance(instanceClass.getClassLoader(),
                    instanceClass.getInterfaces(),
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            System.out.println("执行方法钱");
                            String result = (String)method.invoke(bean, args);
                            System.out.println("执行方法后");
                            return null;
                        }
                    });
        }
        return bean;
    }
}
