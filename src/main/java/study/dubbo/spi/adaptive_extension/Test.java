package study.dubbo.spi.adaptive_extension;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.ProxyFactory;

public class Test {
    //dubbo的自适应扩展
    //实现类相对较多,在调用时对实现类进行加载
    //要调用方法时加载, 不加载也调用不了 自相矛盾
    //dubbo的解决办法是 接口创建一个代理类,通过代理类根据传入的URL创建根据  spi创建指定对象
    //com.alibaba.dubbo.rpc.Protocol
    //Protocol 中有两个 @Adaptive 分别是 export 和 refer
    //export   获得url中的协议,根据协议创建Protocol, 执行创建的对象Protocol的refer方法  extension.refer(arg0, arg1);
    //refer    获得url中的协议,根据协议创建Protocol, 执行创建的对象Protocol的refer方法

    //com.alibaba.dubbo.rpc.cluster.Cluster
    //Cluster 的 join 方法  也有 @Adaptive
    //根据参数中的url 创建 Cluster,执行创建的对象Cluster的join方法
    public static void main(String[] args) {
        ExtensionLoader<Happy> extensionLoader =
                ExtensionLoader.getExtensionLoader(Happy.class);

        Happy haha = extensionLoader.getAdaptiveExtension();

        URL url = URL.valueOf("test://localhost/test?myAdaptiveName=haha");
        haha.say(url);

        //在实现类上可以添加 @Activate注解
        //获取时直接通过@Activate 中的值获得具体的实现类  然后用如下方法获取
        extensionLoader.getActivateExtension(url,new String[]{},"");

        //dubbo 解析xml 使用的类 DubboNamespaceHandler

        //自适应创建 Protocol对象
        Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();


    }

    /**
     * Protocol 类生成的代理  的代码
     */

    public class Protocol$Adpative implements com.alibaba.dubbo.rpc.Protocol {
        public void destroy() {
            throw new UnsupportedOperationException("method public abstract void com.alibaba.dubbo.rpc.Protocol.destroy() of interface com.alibaba.dubbo.rpc.Protocol is not adaptive method!");
        }

        public int getDefaultPort() {
            throw new UnsupportedOperationException("method public abstract int com.alibaba.dubbo.rpc.Protocol.getDefaultPort() of interface com.alibaba.dubbo.rpc.Protocol is not adaptive method!");
        }

        public com.alibaba.dubbo.rpc.Invoker refer(java.lang.Class arg0, com.alibaba.dubbo.common.URL arg1) throws com.alibaba.dubbo.rpc.RpcException {
            if (arg1 == null) throw new IllegalArgumentException("url == null");
            com.alibaba.dubbo.common.URL url = arg1;
            String extName = (url.getProtocol() == null ? "dubbo" : url.getProtocol());
            if (extName == null)
                throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.rpc.Protocol) name from url(" + url.toString() + ") use keys([protocol])");
            com.alibaba.dubbo.rpc.Protocol extension = (com.alibaba.dubbo.rpc.Protocol) ExtensionLoader.getExtensionLoader(com.alibaba.dubbo.rpc.Protocol.class).getExtension(extName);
            return extension.refer(arg0, arg1);
        }

        public com.alibaba.dubbo.rpc.Exporter export(com.alibaba.dubbo.rpc.Invoker arg0) throws com.alibaba.dubbo.rpc.RpcException {
            if (arg0 == null) throw new IllegalArgumentException("com.alibaba.dubbo.rpc.Invoker argument == null");
            if (arg0.getUrl() == null)
                throw new IllegalArgumentException("com.alibaba.dubbo.rpc.Invoker argument getUrl() == null");
            com.alibaba.dubbo.common.URL url = arg0.getUrl();
            String extName = (url.getProtocol() == null ? "dubbo" : url.getProtocol());
            if (extName == null)
                throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.rpc.Protocol) name from url(" + url.toString() + ") use keys([protocol])");
            com.alibaba.dubbo.rpc.Protocol extension = (com.alibaba.dubbo.rpc.Protocol) ExtensionLoader.getExtensionLoader(com.alibaba.dubbo.rpc.Protocol.class).getExtension(extName);
            return extension.export(arg0);
        }
    }
}
