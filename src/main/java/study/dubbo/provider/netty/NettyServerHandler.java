package study.dubbo.provider.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import study.dubbo.provider.code.MessageProtocol;
import study.dubbo.provider.famwork.Invocation;
import study.dubbo.provider.famwork.LocalRegister;
import study.dubbo.provider.tools.ParseByteArray;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;


//服务器这边handler比较简单
public class NettyServerHandler extends SimpleChannelInboundHandler<MessageProtocol>{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol messageProtocol) throws Exception {
        System.out.println("服务端接收客户端请求");
        //接收信息
        int len = messageProtocol.getLen();
        byte[] content = messageProtocol.getContent();
        if(len != content.length){
            System.out.println("服务端发送信息有问题");
        }

        Invocation invocation = (Invocation) ParseByteArray.parseByteArrayToObject(content);
        //从本地 根据方法名获得 Class
        Class clazz = (Class) LocalRegister.localMap.get(invocation.getServiceName());
        //获得要调用的方法
        Method method = clazz.getMethod(invocation.getMethod(), invocation.getParamClazz());
        //执行方法
        String result = (String) method.invoke(clazz.newInstance(), invocation.getParam());
        //将结果返回客户端
        MessageProtocol messageProtocol1 = new MessageProtocol();
        messageProtocol1.setContent(result.getBytes());
        messageProtocol1.setLen(result.getBytes().length);
        ctx.writeAndFlush(messageProtocol1);
    }
}
