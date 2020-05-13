package com.example.demo.study.spring.bean;


public class TeacherFactoryBean implements org.springframework.beans.factory.FactoryBean {

    @Override
    public Object getObject() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setName("李老师");
        return teacher;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
