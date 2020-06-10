package study.dubbo.customer.proxy;

import study.dubbo.customer.netty.NettyClient;
import study.dubbo.provider.famwork.Invocation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyFactory {

    public static <T> T getProxy(Class clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Invocation invocation = new Invocation(clazz.getName(),
                        method.getName(),
                        method.getParameterTypes(),
                        args);
                //Object response = HttpRequest.request("127.0.0.1", 8080, invocation);
                //return response;
                Object response = new NettyClient().start(invocation);
                return response;
            }
        });
    }
}