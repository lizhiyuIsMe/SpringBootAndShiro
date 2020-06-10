package com.example.demo.schedule;

import com.example.demo.util.jvm.JvmInfo;
import com.example.demo.util.redis.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scripting.support.ResourceScriptSource;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


@EnableScheduling
@Configuration
//redis在定时任务中实现分布式锁
public class RedisSchedulingTest {
    private static final Logger logger = LoggerFactory.getLogger(RedisSchedulingTest.class);

    private static String LOCK_PREFIX = "lua_";

    @Autowired
    private  RedisUtils redisUtils;
    @Autowired
    private RedisTemplate redisTemplate;

    //用于执行lua脚本的解释器
    private DefaultRedisScript<Boolean> lockScript;

    @Scheduled(cron="0/10 * * * * *")   //秒分时日月年
    public void  testConTab(){
        String lock = LOCK_PREFIX + "LockNxExJob";
        String lockValue=getHostIp()+"_"+ JvmInfo.jvmPid();

        boolean luaRet = false;
        try {
            luaRet = luaExpress(lock,lockValue);
            //当前锁被人占用
            if (!luaRet) {
                //获得当前redis 价值对中的值
                String value = (String) redisUtils.genValue(lock);
                //这里如果value是本机ip 则可以设置重入,如果一个ip部署多个相同项目则会出现问题
                //所以使用ip+jvmid 来作为value,
                logger.info("lua get lock fail,lock belong to:{}", value);
                if (lockValue.equals(value)) {
                    luaRet=true;
                    //重入时获得锁
                }else{
                    return;
                }
            }
            //获取锁成功
            logger.info("lua start  lock lockNxExJob success");
            Thread.sleep(5000);
        } catch (Exception e) {
            logger.error("lock error", e);
        } finally {
            if (luaRet) {
                logger.info("release lock success");
                //redisUtils.remove(lock);
                boolean flag = releaseLock(lock, lockValue);
                if (flag) {
                    System.out.println("释放锁成功");
                }else{
                    System.out.println("这不是我的锁不能释放");
                }
            }
        }
    }


    /**
     * 获取lua结果
     * @param key
     * @param value
     * @return
     */
    public Boolean luaExpress(String key,String value) {
        lockScript = new DefaultRedisScript<Boolean>();
        lockScript.setScriptSource(
                new ResourceScriptSource(new ClassPathResource("add.lua")));
        lockScript.setResultType(Boolean.class);
        // 封装参数
        List<Object> keyList = new ArrayList<Object>();
        keyList.add(key);
        keyList.add(value);
        Boolean result = (Boolean) redisTemplate.execute(lockScript, keyList);
        return result;
    }

    /**
     * 释放锁操作
     * @param key
     * @param value
     * @return
     */
    private boolean releaseLock(String key, String value) {
        lockScript = new DefaultRedisScript<Boolean>();
        lockScript.setScriptSource(
                new ResourceScriptSource(new ClassPathResource("unlock.lua")));
        lockScript.setResultType(Boolean.class);
        // 封装参数
        List<Object> keyList = new ArrayList<Object>();
        keyList.add(key);
        keyList.add(value);
        Boolean result = (Boolean) redisTemplate.execute(lockScript, keyList);
        return result;
    }

    /**
     * 获取本机内网IP地址方法
     * @return
     */
    private static String getHostIp() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = (InetAddress) addresses.nextElement();
                    if (ip != null
                            && ip instanceof Inet4Address
                            && !ip.isLoopbackAddress() //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                            && ip.getHostAddress().indexOf(":") == -1) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
