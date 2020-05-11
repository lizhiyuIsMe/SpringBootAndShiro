package com.example.demo.config.shiro;

import com.example.demo.config.shiro.filter.CustomRolesOrAuthorizationFilter;
import com.example.demo.config.shiro.realm.CustomRealm;
import com.example.demo.config.shiro.sessionid.CustomCreateSessionid;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig implements EnvironmentAware {

    //在@Configuration配置类中 中配置@Value,在@Configuration方法中得不到配置的值
    //因为加载顺序问题,获得不到值
    //解决办法实现EnvironmentAware 接口,重写setEnvironment方法,在方法中给@Value的属性进行赋值
    @Value("#{config.redis.port}")
    private Integer redisPort;
    @Value("#{config.redis.ip}")
    private String redisIp;
    @Value("#{config.redis.redis.password}")
    private String redisPassword;

    @Override
    public void setEnvironment(Environment environment) {
        this.redisPort = Integer.parseInt(environment.getProperty("config.redis.port"));
        this.redisIp = environment.getProperty("config.redis.ip");
        this.redisPassword =environment.getProperty("config.redis.redis.password");
    }
    /**
     * shiro核心对象,它绑定大部分对象
     */
    @Bean
    public org.apache.shiro.mgt.SecurityManager securityManager(){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //设置认证 和 授权的 realm
        defaultWebSecurityManager.setRealm(customRealm());
        //设置认证 和 授权的缓存
        defaultWebSecurityManager.setCacheManager(cacheManager());
        //设置会话管理  session的缓存方式
        defaultWebSecurityManager.setSessionManager(sessionManager());

        return defaultWebSecurityManager;
    }

    /**
     * ShiroFilterFactoryBean 用来自定义的拦截器都拦截哪些路径
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
        //和securityManager 进行绑定
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        //设置11 个过滤器都拦截什么
        setShiroFilter(shiroFilterFactoryBean);
        return shiroFilterFactoryBean;
    }


//会话管理开始
    /**
     * 会话管理
     * 将登录创建的token存储到redis中
     */
    @Bean
    public SessionManager sessionManager(){
        //默认生成的sessionId 的name 是是JSESSIONID
        //如果想要修改存储的sessionid 的key为token 不为JSESSIONID 则重写这个类中的方法
        DefaultWebSessionManager defaultWebSessionManager=new DefaultWebSessionManager();
        //设置超时时间默认是20秒
        //defaultSessionManager.setGlobalSessionTimeout(20000);
        defaultWebSessionManager.setSessionDAO(getRedisSessionDAO());
        return  defaultWebSessionManager;
    }

    @Bean
    public RedisSessionDAO getRedisSessionDAO(){
        RedisSessionDAO redisSessionDAO=new RedisSessionDAO();
        //自定义设置sessionid的值
        redisSessionDAO.setSessionIdGenerator(customCreateSessionid());

        redisSessionDAO.setRedisManager(getRedisManager());
        return redisSessionDAO;
    }
    //设置创建的sessionid 的值
    @Bean
    public CustomCreateSessionid customCreateSessionid(){
        return  new CustomCreateSessionid();
    }
//会话管理结束

//缓存管理开始
    /**
     * 配置具体cache实现类,缓存管理
     * 这个缓存在认证的时候没有开启,在授权的时候回开启
     */
    @Bean
    public RedisCacheManager cacheManager()
    {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(getRedisManager());
        //设置过期时间，单位是秒，20s,
        redisCacheManager.setExpire(20);
        return redisCacheManager;
    }
    /**
     * 配置redisManager
     */
    @Bean
    public RedisManager getRedisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisIp);
        redisManager.setPort(redisPort);
        redisManager.setPassword(redisPassword);
        return redisManager;
    }
//缓存管理结束

    /**
     * 自己定义的realm 用于  认证和授权
     * @return
     */
    @Bean
    public CustomRealm customRealm(){
        CustomRealm customRealm=new CustomRealm();

        //可以设置加密方法
        //customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return customRealm;
    }


    //这个是指定密码加密的方法
    //这个对象是在认证时候的时候可以使用  在认证Realm中有这个属性
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();

        //设置散列算法：这里使用的MD5算法
        credentialsMatcher.setHashAlgorithmName("md5");

        //散列次数，好比散列2次，相当于md5(md5(xxxx))
        credentialsMatcher.setHashIterations(2);
//        注册时候直接创建密码 的加密过程
//        String hashName = "md5";
//        String pwd = "123";
//        Object result = new SimpleHash(hashName, pwd, null, 2);
//        System.out.println(result);

        return credentialsMatcher;
    }
    private void setShiroFilter(ShiroFilterFactoryBean shiroFilterFactoryBean) {

        //org.apache.shiro.web.filter.mgt.DefaultFilter  //拦截器枚举类

        //没有登录会访问下面url (如果不是前后端分离，则跳转页面)
        shiroFilterFactoryBean.setLoginUrl("/pub/need_login");

        //登录成功，跳转url，如果前后端分离，则没这个调用
        shiroFilterFactoryBean.setSuccessUrl("/");

        //没有权限，未授权就跳转到这个url， 先验证登录-》再验证是否有权限
        shiroFilterFactoryBean.setUnauthorizedUrl("/pub/not_permit");


        //创建自定义拦截器,将自定义拦截器添加到shiroFilterFactoryBean对象中
        //就好比在DefaultFilter中添加了个枚举，然后下面就可以使用这个自定义的Filter了
        addFilterType(shiroFilterFactoryBean);

        //大坑1 :这里必须要LinkedHashMap 用hashMap 会导致拦截失效
        //大坑2 :从上向下一次进行拦截,如果一个拦截器拦截到了的话 并且返回true则代表有权限
        LinkedHashMap<String,String> filterChainDefinitionMap=new LinkedHashMap<String,String>();

        //退出过滤器
        //退出的逻辑不用写了,shiro写好了,直接调用这个二地址就好了
        filterChainDefinitionMap.put("/logout","logout");

        //匿名可以访问，也是就游客模式
        filterChainDefinitionMap.put("/pub/**,/test/**","anon");
        //filterChainDefinitionMap.put("/pub/**","xssFilter");

        //登录用户才可以访问
        filterChainDefinitionMap.put("/authc/**","authc");

        //管理员角色才可以访问  这里使用的自己定义的filter
        filterChainDefinitionMap.put("/admin/**","roleOrFilter[admin,root]");
        //使用系统配置的filter
        //filterChainDefinitionMap.put("/admin/**","roles[admin,root]");

        //有编辑权限才可以访问
        filterChainDefinitionMap.put("/video/update","perms[video_update]");

        //authc : url定义必须通过认证才可以访问
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    }

    private void addFilterType(ShiroFilterFactoryBean shiroFilterFactoryBean) {
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        //添加自己定义的filter    假设枚举类型为 roleOrFilter
        filterMap.put("roleOrFilter",new CustomRolesOrAuthorizationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
    }

    // 下面配置的不用管了,配上就行

    /**
     * 管理shiro一些bean的生命周期 即bean初始化 与销毁
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    /**
     *  api controller 层面
     *  加入注解的使用，不加入这个AOP注解不生效(shiro的注解 例如 @RequiresGuest)
     *
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }


    /**
     *  用来扫描上下文寻找所有的Advistor(通知器),
     *  将符合条件的Advisor应用到切入点的Bean中，需要在LifecycleBeanPostProcessor创建后才可以创建
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }


}
