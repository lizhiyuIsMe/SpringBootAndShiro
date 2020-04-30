package com.example.demo.dao;

import com.example.demo.domain.Users;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;


public interface UserMapper {

    @Select("select * from users where username = #{username}")
    Users findByUsername(@Param("username")String username);



    @Select("select * from users where id=#{userId}")
    @Results(
            value = {
                    @Result(id=true, property = "id",column = "id"),
                    @Result(property = "username",column = "username"),
                    @Result(property = "password",column = "password"),
                    @Result(property = "createTime",column = "createTime"),
                    @Result(property = "salt",column = "salt"),
                    @Result(property = "roleList",column = "id",
                            many = @Many(select = "com.example.demo.dao.RoleMapper.findRoleListByUserId", fetchType = FetchType.DEFAULT)
                    )
            }
    )
    Users findById(@Param("userId") String id);



    @Select("select * from users where username = #{username} and password = #{pwd}")
    Users findByUsernameAndPwd(@Param("username")String username, @Param("pwd")String pwd);



}
