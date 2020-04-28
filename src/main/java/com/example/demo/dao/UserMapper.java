package com.example.demo.dao;

import com.example.demo.domain.Users;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


public interface UserMapper {

    @Select("select * from users where username = #{username}")
    Users findByUsername(@Param("username")String username);



    @Select("select * from users where id=#{userId}")
    Users findById(@Param("userId") String id);



    @Select("select * from users where username = #{username} and password = #{pwd}")
    Users findByUsernameAndPwd(@Param("username")String username, @Param("pwd")String pwd);



}
