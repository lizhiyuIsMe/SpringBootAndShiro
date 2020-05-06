package com.example.demo.config.shiro.sessionid;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * 自己定义创建的sessionid 的值是多少
 * redisSessionDao  用于将sessionid持久化到redis数据库,里面有对应生成sessionid 的方法
 * 原来创建创建sessionid 的方法现在要将它覆盖 private SessionIdGenerator sessionIdGenerator = new JavaUuidSessionIdGenerator();
 */
public class CustomCreateSessionid implements SessionIdGenerator {

    /**
     * token中不要含有敏感信息,不要有密码和权限信息,为了防止别人修改
     */
    @Override
    public Serializable generateId(Session session) {
        return "lizhiyu:"+ UUID.randomUUID().toString().replace("-","");
    }
}
