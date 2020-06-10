package study.dubbo.provider;


import study.dubbo.provider.api.ServiceImpl;
import study.dubbo.provider.famwork.LocalRegister;
import study.dubbo.provider.netty.NettyServer;

public class Server {
    public static void main(String[] args) {
        //注册本地服务
        LocalRegister.register(ServiceImpl.class.getName(),ServiceImpl.class);
        //注册远程服务
        //RemoteRegister.register(Service.class.getName(),new Url("",""));
        //Http http = new Http("127.0.0.1",8080);
        new NettyServer("127.0.0.1", 8080);
    }
}
