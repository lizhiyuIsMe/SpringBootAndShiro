package study.dubbo.spi.exportservice;

//dubbo 的服务导出   ServiceBean  <dubbo:service/> 中的信息
//在启动项目进行服务导出  分为三步
//1 参数检查 URL初始化
//2 将服务导入到本地   同时将服务导出到远程   Invoker 创建过程
//3 向注册中心注册服务
public class Test {
    public static void main(String[] args) {
        //最主要的两个类 用于dubbo整合spring(ServiceBean、ReferenceBean)
        //<dubbo:provider export="false" />   dubbo 拒绝导出服务的配置


        //<dubbo:registry/>  注册中心配置 放在 RegistryConfig 中
        //<dubbo:provider/>  提供者            ProviderConfig
        //<dubbo:consumer/>  消费者            ConsumerConfig


        //ApplicationConfig
        //ProtocolConfig
        //ModuleConfig
        //<dubbo:service/>   ServiceConfig
            // 一个服务可以注册多个协议   一个服务可以注册到多个注册中心   //一个服务在一个注册中心可以有多个协议? 怎么配
        //<dubbo:method> 标签的配置信息  MethodConfig

    }
}
