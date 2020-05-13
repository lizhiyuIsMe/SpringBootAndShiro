package com.example.demo.study.spring;

import com.example.demo.study.spring.bean.Student;
import com.example.demo.study.spring.bean.Teacher;
import com.example.demo.study.spring.service.TestServiceInter;
import com.example.demo.study.spring.util.BeanDefined;
import com.example.demo.study.spring.util.BeanFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws Exception {
        //定义一个bean
        BeanDefined beanDefined=new BeanDefined();
        beanDefined.setBeadId("student");
        beanDefined.setBeanClassPath("com.example.demo.study.spring.bean.Student");
        //属性赋值
        Map<String,String> property=new HashMap<String,String>();
        property.put("name","李同学");
        beanDefined.setProperty(property);

        //定义一个bean
        BeanDefined beanDefinedC=new BeanDefined();
        beanDefinedC.setBeadId("testService");
        beanDefinedC.setBeanClassPath("com.example.demo.study.spring.service.TestService");

        //定义一个factoryBean  factoryBean的类型必须是prototype
        BeanDefined beanDefinedA=new BeanDefined();
        beanDefinedA.setBeadId("teacherFactoryBean");
        beanDefinedA.setBeanClassPath("com.example.demo.study.spring.bean.TeacherFactoryBean");
        beanDefinedA.setIsFactoryBean(true);
        beanDefinedA.setScope("prototype");

        //定义一个后置处理器
        BeanDefined beanDefinedB=new BeanDefined();
        beanDefinedB.setBeadId("myBeanPostProcess");
        beanDefinedB.setBeanClassPath("com.example.demo.study.spring.util.MyBeanPostProcess");


        //将对象放到ioc容器中
        List<BeanDefined> list=new ArrayList<BeanDefined>();
        list.add(beanDefined);
        list.add(beanDefinedA);
        list.add(beanDefinedB);
        list.add(beanDefinedC);

        //创建ioc容器对象
        BeanFactory beanFactory = new BeanFactory(list);


        Student student = (Student)beanFactory.getBeanDefined("student");
        System.out.println(student.getName());
        Teacher teacher = (Teacher)beanFactory.getBeanDefined("teacherFactoryBean");
        //必须用接口接收
        TestServiceInter testService = (TestServiceInter)beanFactory.getBeanDefined("testService");
        testService.say();


    }
}
